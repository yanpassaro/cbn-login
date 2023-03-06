package com.cbn.login.service;

import com.cbn.login.domain.Account;
import com.cbn.login.domain.Token;
import com.cbn.login.domain.dto.AccountDTO;
import com.cbn.login.domain.dto.LoginDTO;
import com.cbn.login.exception.IncorrectCredentialsException;
import com.cbn.login.exception.NotAuthorizedException;
import com.cbn.login.repository.AccountRepository;
import com.cbn.login.repository.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDate.now;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MessageService messageService;

    @Transactional
    public void register(AccountDTO accountDTO) throws IncorrectCredentialsException {
        if (accountRepository.existsByEmail(accountDTO.email())) {
            log.warn("Registration attempt failed");
            throw new IncorrectCredentialsException("Account has already been registered");
        }
        log.info("Registering new account {}", accountDTO);
        Account account = Account.builder()
                .email(accountDTO.email())
                .password(accountDTO.password())
                .token(UUID.randomUUID().toString())
                .expiration(LocalDate.now().plusDays(5).toString())
                .build();
        rabbitTemplate.convertAndSend("account", accountDTO);
        messageService.sendEmail(account.getEmail(), "Email verification", "http://localhost:8080/verify/" + account.getToken());
    }

    @Transactional
    public String login(LoginDTO loginDTO) throws IncorrectCredentialsException, NotAuthorizedException {
        if (!accountRepository.existsByEmail(loginDTO.email()))
            throw new NotAuthorizedException("Incorrect credentials");
        if (!accountRepository.existsByEmailAndPassword(loginDTO.email(), loginDTO.password()))
            throw new IncorrectCredentialsException("Incorrect credentials");
        Account account = accountRepository.findByEmail(loginDTO.email());
        if (!account.isVerified()) {
            newEmailVerification(account);
        }
        if (LocalDate.parse(account.getExpiration()).isBefore(now())) {
            account.setToken(UUID.randomUUID().toString());
            account.setExpiration(LocalDate.now().plusDays(5).toString());
            return accountRepository.save(account).getToken();
        }
        return account.getToken();
    }

    @Transactional
    public void newEmailVerification(Account account) throws NotAuthorizedException {
        Optional<Token> token = tokenRepository.findById(UUID.fromString(account.getToken()));
        if (token.isEmpty()) {
            log.info("New email verification token for {}", account);
            Token newToken = tokenRepository.save(
                    Token.builder()
                            .id(UUID.randomUUID())
                            .account(account.getId())
                            .build());
            throw new NotAuthorizedException("Account not verified, new verification token sent to email");
        }
        throw new NotAuthorizedException("Account not verified, verify your email");
    }

    @Transactional
    public void logout(String token) throws NotAuthorizedException {
        if (!accountRepository.existsByToken(token))
            throw new NotAuthorizedException("Token is invalid");
        Account account = accountRepository.findByToken(token);
        account.setToken(null);
        account.setExpiration(LocalDate.now().plusDays(-1).toString());
        accountRepository.save(account);
    }

    @Transactional
    public void verify(UUID token) throws NotAuthorizedException {
        if (!tokenRepository.existsById(token))
            throw new NotAuthorizedException("Token is invalid, please request a new one");
        Token token1 = tokenRepository.findById(token).orElseThrow(
                () -> new NotAuthorizedException("Token is invalid") );
        Account account = accountRepository.findById(token1.getAccount()).orElseThrow(
                () -> new NotAuthorizedException("Account not found"));
        account.setVerified(true);
        accountRepository.save(account);
    }
}

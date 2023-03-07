package com.cbn.login.service;

import com.cbn.login.domain.Account;
import com.cbn.login.domain.Application;
import com.cbn.login.domain.AppAuthorization;
import com.cbn.login.domain.dto.AccountDTO;
import com.cbn.login.domain.dto.Login;
import com.cbn.login.exception.IncorrectCredentialsException;
import com.cbn.login.exception.NotAuthorizedException;
import com.cbn.login.repository.AccountRepository;
import com.cbn.login.repository.AuthorizationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.now;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AuthorizationRepository authorizationRepository;

    @Transactional
    public void register(AccountDTO accountDTO) throws IncorrectCredentialsException {
        if (accountRepository.existsByEmail(accountDTO.email())) {
            log.warn("Registration attempt failed");
            throw new IncorrectCredentialsException("Account has already been registered");
        }
        log.info("Registering new account {}", accountDTO);
        accountRepository.save(Account.builder()
                .name(accountDTO.name())
                .lastName(accountDTO.lastName())
                .email(accountDTO.email())
                .password(accountDTO.password())
                .appAuthorization(new ArrayList<>())
                .build());
    }

    @Transactional
    public Account login(Application application, Login login) throws IncorrectCredentialsException, NotAuthorizedException {
        if (!accountRepository.existsByEmail(login.email()))
            throw new NotAuthorizedException("Incorrect credentials");
        if (!accountRepository.existsByEmailAndPassword(login.email(), login.password()))
            throw new IncorrectCredentialsException("Incorrect credentials");
        if (accountRepository.existsByEmailAndAppAuthorizationService(login.email(), application.name()))
            throw new NotAuthorizedException("Application already authorized");
        Account account = accountRepository.findByEmail(login.email());
        account.getAppAuthorization().add(authorizationRepository.save(AppAuthorization.builder()
                .service(application.name())
                .token(UUID.randomUUID())
                .expirationDate(now().plusDays(30))
                .build()));
        return accountRepository.save(account);
    }
}

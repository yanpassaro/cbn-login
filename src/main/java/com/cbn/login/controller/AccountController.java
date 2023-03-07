package com.cbn.login.controller;

import com.cbn.login.domain.Application;
import com.cbn.login.domain.Response;
import com.cbn.login.domain.dto.AccountDTO;
import com.cbn.login.domain.dto.Login;
import com.cbn.login.exception.IncorrectCredentialsException;
import com.cbn.login.exception.NotAuthorizedException;
import com.cbn.login.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Response> register(@RequestBody @Valid AccountDTO accountDTO)
            throws IncorrectCredentialsException {
        accountService.register(accountDTO);
        return ResponseEntity.ok().body(Response.builder()
                .status(OK).statusCode(OK.value())
                .message("Registered user")
                .build());
    }

    @PostMapping("/login")
        public ResponseEntity<Response> login(@RequestParam Application application,
                                              @RequestBody @Valid Login login)
            throws IncorrectCredentialsException, NotAuthorizedException {
        return ResponseEntity.ok().body(Response.builder()
                .status(OK).statusCode(OK.value())
                .message("Account vinculated to application")
                .data(Map.of("Account", accountService.login(application, login)))
                .build());
    }
}
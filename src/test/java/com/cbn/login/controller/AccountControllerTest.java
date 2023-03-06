package com.cbn.login.controller;

import com.cbn.login.domain.dto.AccountDTO;
import com.cbn.login.domain.dto.LoginDTO;
import com.cbn.login.exception.IncorrectCredentialsException;
import com.cbn.login.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(PER_CLASS)
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountService accountService;
    AccountDTO accountDTO;
    LoginDTO loginDTO;

    @BeforeAll
    void setUp() throws IncorrectCredentialsException {
        accountService.register(new AccountDTO(null, "name", "email@email", "password1"));
    }

    @Test
    void accountIntegration() throws Exception {
        accountDTO = new AccountDTO(null, "name", "email@email.com", "password1");
        loginDTO = new LoginDTO("email@email.com", "password1");
        mockMvc.perform(post("/api/account/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(post("/api/account/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testAccountAlreadyExists() throws Exception {
        mockMvc.perform(post("/api/account/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testCreateAccountWithIncorrectCredentials() throws Exception {
        accountDTO = new AccountDTO(null, "name", "email", "password");
        mockMvc.perform(post("/api/account/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testLoginFailedByEmail() throws Exception {
        loginDTO = new LoginDTO("incorrect@email", "password");
        mockMvc.perform(post("/api/account/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testLoginFailedByPassword() throws Exception {
        loginDTO = new LoginDTO("email@email", "incorrect");
        mockMvc.perform(post("/api/account/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
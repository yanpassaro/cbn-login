package com.cbn.login.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record AccountDTO(
        Long id,
        @NotNull @Size(min = 3, max = 30)
        String name,
        @Email
        String email,
        @NotNull @Size(min = 8, max = 30)
        String password

) implements Serializable {
}
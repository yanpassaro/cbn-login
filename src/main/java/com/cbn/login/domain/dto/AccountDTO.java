package com.cbn.login.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record AccountDTO(
        @NotNull @Size(min = 3, max = 30)
        String name,
        @NotNull @Size(min = 3, max = 30)
        String lastName,
        @Email
        String email,
        @NotNull @Size(min = 8, max = 30)
        String password

) implements Serializable {
}
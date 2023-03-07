package com.cbn.login.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppAuthorization {
    @Id
    @GeneratedValue
    private Long id;
    private String service;
    private UUID token;
    private LocalDate expirationDate;
}

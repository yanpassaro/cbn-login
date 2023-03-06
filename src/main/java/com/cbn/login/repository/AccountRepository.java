package com.cbn.login.repository;

import com.cbn.login.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndPassword(String email, String password);
    Account findByEmail(String email);

    Account findByToken(String token);

    boolean existsByToken(String token);
}
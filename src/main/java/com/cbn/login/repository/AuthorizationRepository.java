package com.cbn.login.repository;

import com.cbn.login.domain.AppAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationRepository extends JpaRepository<AppAuthorization, Long> {
}

package com.cbn.login.repository;

import com.cbn.login.domain.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TokenRepository extends CrudRepository<Token, UUID> {
}

package com.cbn.login.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash(timeToLive = 60 * 60 * 2)
@Builder
@Data
public class Token {
    @Id
    private UUID id;
    private Long account;
}

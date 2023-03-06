package com.cbn.login.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@Builder
public class Response {
    private Integer statusCode;
    private HttpStatus status;
    private String message;
    private Map<String, ?> data;
}

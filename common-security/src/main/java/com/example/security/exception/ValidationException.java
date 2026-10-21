package com.example.security.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ValidationException extends ApiException {
    public ValidationException(List<String> messages) {
        super(HttpStatus.BAD_REQUEST, messages);
    }
}

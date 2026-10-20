package com.example.common_security.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiExceptionResponder {

    private ApiExceptionResponder() {
    }

    public static ResponseEntity<ApiError> respond(Throwable ex, HttpServletRequest request) {
        HttpStatus status = ApiErrorFactory.status(ex);
        ApiErrorFactory.log(ex, status, request.getRequestURI());
        return ResponseEntity.status(status).body(ApiErrorFactory.body(ex));
    }
}

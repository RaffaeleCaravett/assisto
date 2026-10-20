package com.example.common_security.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final List<String> messages;

    protected ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.messages = List.of(message);
    }

    protected ApiException(HttpStatus status, List<String> messages) {
        super(messages == null || messages.isEmpty() ? SpokenErrors.GENERIC : messages.get(0));
        this.status = status;
        this.messages = messages == null || messages.isEmpty()
                ? List.of(SpokenErrors.GENERIC)
                : List.copyOf(messages);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getMessages() {
        return messages;
    }
}

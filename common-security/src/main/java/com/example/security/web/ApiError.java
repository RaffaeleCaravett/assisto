package com.example.security.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.security.exception.SpokenErrors;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String message, List<String> messageList) {

    public static ApiError of(String message) {
        String spoken = (message == null || message.isBlank()) ? SpokenErrors.GENERIC : message;
        return new ApiError(spoken, null);
    }

    public static ApiError of(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return of(SpokenErrors.GENERIC);
        }
        List<String> cleaned = messages.stream()
                .filter(item -> item != null && !item.isBlank())
                .toList();
        if (cleaned.isEmpty()) {
            return of(SpokenErrors.GENERIC);
        }
        if (cleaned.size() == 1) {
            return of(cleaned.get(0));
        }
        return new ApiError(null, List.copyOf(cleaned));
    }
}

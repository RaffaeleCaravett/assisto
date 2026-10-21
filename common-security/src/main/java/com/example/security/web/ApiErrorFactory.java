package com.example.security.web;

import com.example.security.exception.ApiException;
import com.example.security.exception.SpokenErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

public final class ApiErrorFactory {

    private static final Logger log = LoggerFactory.getLogger(ApiErrorFactory.class);

    private ApiErrorFactory() {
    }

    public static HttpStatus status(Throwable ex) {
        if (ex instanceof ApiException api) {
            return api.getStatus();
        }
        if (ex instanceof ResponseStatusException rse) {
            return HttpStatus.valueOf(rse.getStatusCode().value());
        }
        if (ex instanceof MethodArgumentNotValidException
                || ex instanceof BindException
                || ex instanceof ConstraintViolationException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (ex instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        if (ex instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public static ApiError body(Throwable ex) {
        if (ex instanceof ApiException api) {
            return ApiError.of(api.getMessages());
        }
        if (ex instanceof MethodArgumentNotValidException manve) {
            return ApiError.of(fieldMessages(manve.getBindingResult().getFieldErrors()));
        }
        if (ex instanceof BindException bind) {
            return ApiError.of(fieldMessages(bind.getFieldErrors()));
        }
        if (ex instanceof ConstraintViolationException cve) {
            List<String> messages = cve.getConstraintViolations().stream()
                    .map(ApiErrorFactory::violationMessage)
                    .toList();
            return ApiError.of(messages);
        }
        if (ex instanceof ResponseStatusException rse) {
            return ApiError.of(spokenReason(rse.getStatusCode(), rse.getReason()));
        }
        if (ex instanceof AccessDeniedException) {
            return ApiError.of(SpokenErrors.FORBIDDEN);
        }
        if (ex instanceof AuthenticationException) {
            return ApiError.of(SpokenErrors.SESSION);
        }
        return ApiError.of(SpokenErrors.GENERIC);
    }

    public static void log(Throwable ex, HttpStatus status, String path) {
        String type = ex.getClass().getSimpleName();
        String dettaglio = ex.getMessage();
        if (status.is5xxServerError()) {
            log.error("Errore API status={} path={} tipo={} dettaglio={}", status.value(), path, type, dettaglio, ex);
            return;
        }
        log.warn("Errore API status={} path={} tipo={} dettaglio={}", status.value(), path, type, dettaglio);
    }

    private static List<String> fieldMessages(List<FieldError> errors) {
        List<String> messages = new ArrayList<>();
        for (FieldError error : errors) {
            messages.add(fieldMessage(error));
        }
        return messages;
    }

    private static String fieldMessage(FieldError error) {
        String field = spokenField(error.getField());
        String code = error.getCode();
        if ("NotBlank".equals(code) || "NotNull".equals(code) || "NotEmpty".equals(code)) {
            return "Il campo " + field + " è obbligatorio.";
        }
        if ("Email".equals(code)) {
            return "Inserisci un indirizzo email valido.";
        }
        if ("Size".equals(code)) {
            return "Il campo " + field + " non ha la lunghezza giusta.";
        }
        String defaultMessage = error.getDefaultMessage();
        if (defaultMessage != null && defaultMessage.contains(" ")) {
            return defaultMessage;
        }
        return "Il campo " + field + " non è valido.";
    }

    private static String violationMessage(ConstraintViolation<?> violation) {
        String field = spokenField(violation.getPropertyPath().toString());
        return "Il campo " + field + " non è valido.";
    }

    private static String spokenField(String field) {
        if (field == null) {
            return "richiesto";
        }
        String last = field.contains(".") ? field.substring(field.lastIndexOf('.') + 1) : field;
        return switch (last) {
            case "email" -> "email";
            case "password" -> "password";
            case "nome" -> "nome";
            case "cognome" -> "cognome";
            case "tel" -> "telefono";
            case "descrizione" -> "descrizione";
            case "dataEvento" -> "data dell'evento";
            case "categoria" -> "categoria";
            case "livelloEsperienza" -> "livello";
            case "gruppiMuscolari" -> "gruppi muscolari";
            case "tipoEvento" -> "tipo di evento";
            default -> last;
        };
    }

    private static String spokenReason(HttpStatusCode status, String reason) {
        if (reason != null && !reason.isBlank()) {
            return reason;
        }
        int code = status.value();
        if (code == 401) {
            return SpokenErrors.SESSION;
        }
        if (code == 403) {
            return SpokenErrors.FORBIDDEN;
        }
        if (code == 404) {
            return SpokenErrors.NOT_FOUND;
        }
        if (code == 409) {
            return SpokenErrors.EMAIL_TAKEN;
        }
        if (code == 400) {
            return SpokenErrors.BAD_REQUEST;
        }
        if (code == 503) {
            return SpokenErrors.UNAVAILABLE;
        }
        return SpokenErrors.GENERIC;
    }
}

package com.example.security;

import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

public final class AppCors {

    public static final String DEFAULT_ORIGINS =
            "http://localhost:8100,http://127.0.0.1:8100,http://localhost:4200,http://127.0.0.1:4200,capacitor://localhost,ionic://localhost";

    private AppCors() {
    }

    public static CorsConfiguration of(String raw) {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = parse(raw);
        if (origins.isEmpty()) {
            origins = parse(DEFAULT_ORIGINS);
        }
        origins.forEach(config::addAllowedOriginPattern);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }

    public static List<String> parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .filter(value -> !"*".equals(value) && !"/**".equals(value))
                .toList();
    }
}

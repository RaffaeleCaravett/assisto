package com.example.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Ripubblica l'identita' presa dalle claim verificate come header X-User-*.
 * I controller continuano a leggerla con @RequestHeader, ma il valore non
 * arriva piu' dalla rete: arriva dalla firma del gateway.
 */
public final class IdentityHeaders {

    public static final String USER_ID = "X-User-Id";
    public static final String USER_ROLE = "X-User-Role";

    private IdentityHeaders() {
    }

    public static HttpServletRequest wrap(HttpServletRequest request, Claims claims) {
        String userId = claims.getSubject();
        String role = asString(claims.get("role"));

        return new HttpServletRequestWrapper(request) {

            @Override
            public String getHeader(String name) {
                String value = fromClaims(name);
                return value != null ? value : super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                String value = fromClaims(name);
                return value != null ? Collections.enumeration(List.of(value)) : super.getHeaders(name);
            }

            private String fromClaims(String name) {
                if (USER_ID.equalsIgnoreCase(name)) {
                    return userId;
                }
                if (USER_ROLE.equalsIgnoreCase(name)) {
                    return role;
                }
                return null;
            }
        };
    }

    private static String asString(Object value) {
        return (value == null || "null".equals(String.valueOf(value))) ? null : String.valueOf(value);
    }
}

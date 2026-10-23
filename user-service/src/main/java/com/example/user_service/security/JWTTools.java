package com.example.user_service.security;

import com.example.user_service.utente.UtenteService;
import com.example.security.dtos.Token;
import com.example.security.enums.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTTools {
    @Value("${spring.jwt.secret}")
    private String secret;
    private final UtenteService userService;

    public Token createTokens(Long id, String type) {

        Map<String, Object> accessMap = new HashMap<>();
        accessMap.put("type", TokenType.ACCESS);
        accessMap.put("role", type);
        Map<String, Object> refreshMap = new HashMap<>();
        refreshMap.put("role", type);
        refreshMap.put("type", TokenType.REFRESH);

        String accessToken = Jwts.builder()
                .claims(accessMap)
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .issuer("user-service")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();

        String refreshToken = Jwts.builder()
                .claims(refreshMap)
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .issuer("user-service")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();

        return new Token(accessToken, refreshToken);
    }
}
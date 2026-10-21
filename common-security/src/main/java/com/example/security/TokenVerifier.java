package com.example.security;

import com.example.security.enums.TokenType;
import com.example.security.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TokenVerifier {
    @Value("${spring.jwt.secret}")
    private String secret;


    public Claims verifyRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .requireIssuer("user-service")
                    .clockSkewSeconds(30)
                    .build().parseSignedClaims(token).getPayload();
            if (!TokenType.REFRESH.name().equals(claims.get("type")) || !"user-service".equals(claims.getIssuer())) {
                throw new UnauthorizedException("Accedi nuovamente.");
            }
            if (!"USER".equals(claims.get("role"))
                    && !"ADMIN".equals(claims.get("role"))) {
                throw new UnauthorizedException("Accedi nuovamente.");
            }
            return claims;
        } catch (Exception e) {
            throw new UnauthorizedException("Accedi nuovamente.");
        }
    }

    public Claims verifyAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .requireIssuer("user-service")
                    .clockSkewSeconds(30)
                    .build().parseSignedClaims(token).getPayload();
            if (!TokenType.ACCESS.name().equals(claims.get("type")) || !"user-service".equals(claims.getIssuer())) {
                throw new UnauthorizedException("Accedi nuovamente.");
            }
            if (!"USER".equals(claims.get("role"))
                    && !"ADMIN".equals(claims.get("role"))) {
                throw new UnauthorizedException("Accedi nuovamente.");
            }
            return claims;
        } catch (Exception e) {
            throw new UnauthorizedException("Accedi nuovamente.");
        }
    }
    public String extractIdFromToken(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseSignedClaims(token).getPayload().getSubject();
    }
}
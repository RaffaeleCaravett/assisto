package com.example.gateway.filter;

import com.example.security.TokenVerifier;
import com.example.security.exception.SpokenErrors;
import com.example.security.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final ReactiveStringRedisTemplate redis;
    private final TokenVerifier tokenVerifier;

    public JwtAuthFilter(
            ReactiveStringRedisTemplate redis,
            TokenVerifier tokenVerifier
    ) {
        this.redis = redis;
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (HttpMethod.OPTIONS.equals(request.getMethod()) || isPublic(path)) {

            return chain.filter(
                    exchange.mutate()
                            .request(withInternal(exchange, null))
                            .build()
            );
        }


        String token = bearerToken(request);
        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        Claims claims = null;
        try {
            claims = tokenVerifier.verifyAccessToken(token);
        } catch (JwtException | UnauthorizedException | IllegalArgumentException ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String jti = claims.getId();
        String blacklistKey = "jwt:blacklist:";
        if (jti != null)
            blacklistKey += claims.getId();
        Claims finalClaims = claims;
        return redis.hasKey(blacklistKey).flatMap(blacklisted -> {
            if (Boolean.TRUE.equals(blacklisted) && jti != null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            ServerHttpRequest mutated = withInternal(
                    exchange,
                    finalClaims
            );
            return chain.filter(exchange.mutate().request(mutated).build());
        });

    }

    private ServerHttpRequest withInternal(ServerWebExchange exchange, Claims claims) {
        String audience = targetService(exchange);

        return exchange.getRequest().mutate().headers(headers -> {
            headers.remove("X-User-Id");
            headers.remove("X-User-Role");
            if (claims != null) {
                String userId = claims.getSubject();
                if (userId != null && !userId.isBlank()) {
                    headers.set("X-User-Id", userId);
                }
                String role = asString(claims.get("role"));
                if (role != null) {
                    headers.set("X-User-Role", role);
                }
            }
        }).build();
    }

    private String targetService(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null && route.getUri().getHost() != null) {
            return route.getUri().getHost();
        }
        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/api/users") || path.startsWith("/api/billing")) {
            return "user-service";
        }
        if (path.startsWith("/api/centro-notifiche")) {
            return "centro-notifiche";
        }
        if (path.startsWith("/api/ricettario")) {
            return "ricettario";
        }
        return "unknown";
    }

    private static String asString(Object value) {
        return (value == null || "null".equals(String.valueOf(value))) ? null : String.valueOf(value);
    }

    private String bearerToken(ServerHttpRequest request) {
        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        String queryToken = request.getQueryParams().getFirst("access_token");
        if (queryToken != null && !queryToken.isBlank()) {
            return queryToken;
        }
        return null;
    }

    private boolean isPublic(String path) {
        return path.startsWith("/api/users/auth/")
                || path.startsWith("/fallback/")
                || path.startsWith("/api/billing/webhook")
                || path.startsWith("/actuator/health")
                || path.startsWith("/actuator/prometheus");
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
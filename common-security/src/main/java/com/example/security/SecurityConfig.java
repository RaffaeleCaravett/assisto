package com.example.security;

import com.example.security.web.ReactiveApiErrorWebExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Autowired
    JWTAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disabilitiamo alcuni comportamenti di default
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/users/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        // Aggiungo/rimuovo protezione sui singoli endpoint in maniera che venga/non venga richiesta l'autenticazione per accedervi
        return http.build();
    }

    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(11);
        // 11 è il numero di ROUNDS, ovvero quante volte viene eseguito l'algoritmo. In parole povere ci serve
        // per settare la velocità di esecuzione di bcrypt (+ è alto il numero, + lento l'algoritmo, + sicure sarnno le pw)
    }

    @Bean
    JWTAuthFilter JWTAuthFilter() {
        return new JWTAuthFilter();
    }
}
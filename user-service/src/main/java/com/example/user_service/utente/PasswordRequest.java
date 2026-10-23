package com.example.user_service.utente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordRequest(
        @NotBlank @Size(min = 8, max = 72) String password
) {
}

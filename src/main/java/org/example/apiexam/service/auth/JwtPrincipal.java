package org.example.apiexam.service.auth;

import java.util.List;

public record JwtPrincipal(
        String username,
        List<String> roles
) {
}

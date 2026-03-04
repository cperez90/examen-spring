package org.example.apiexam.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiError(
        int statusCode,
        String error,
        Instant timestamp,
        String message
) {
}

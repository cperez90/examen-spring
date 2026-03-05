package org.example.apiexam.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.example.apiexam.error.BadCredentialsException;
import org.example.apiexam.error.ResourceException;
import org.example.apiexam.error.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ResourceException ex){

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                Instant.now(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {

        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                Instant.now(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleUserBadCredential(BadCredentialsException ex) {

        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                Instant.now(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String param = ex.getName();
        Object value = ex.getValue();
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valor válido";

        String message = "Parámetro inválido: '" + param + "'='" + value + "'. Se esperaba: " + expected;

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Instant.now(),
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}

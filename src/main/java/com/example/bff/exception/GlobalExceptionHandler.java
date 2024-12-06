package com.example.bff.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomClientException.class)
    public ResponseEntity<Object> handleCustomClientException(CustomClientException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", ex.getStatus().value());
        responseBody.put("message", ex.getMessage());

        return new ResponseEntity<>(responseBody, ex.getStatus());
    }
}


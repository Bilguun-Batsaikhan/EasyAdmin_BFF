package com.certimeter.bff.exception;

import com.certimeter.bff.dto.HttpResponse;
import com.certimeter.bff.enumeration.HttpResponseEnum;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(FailureException.class)
    public ResponseEntity<HttpResponse> handleFailureException(FailureException exception) {
        HttpResponseEnum responseEnum = exception.getHttpResponseEnum();

        String message = exception.getCustomMessage() != null ? exception.getCustomMessage() : responseEnum.getDescription();

        if (responseEnum == HttpResponseEnum.AUTHORIZATION_FAILED) {
            HttpResponse failureResponse = new HttpResponse(
                    responseEnum.getId(),
                    responseEnum.getDescription()
            );
            return ResponseEntity.status(responseEnum.getHttpStatus()).body(failureResponse);
        }
        HttpStatus httpStatusOfFailure = responseEnum.getHttpStatus();
        return ResponseEntity.status(httpStatusOfFailure).body(new HttpResponse(responseEnum.getId(), message));
    }
}


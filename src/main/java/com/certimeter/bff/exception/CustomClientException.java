package com.certimeter.bff.exception;

import org.springframework.http.HttpStatusCode;

public class CustomClientException extends RuntimeException {
    private final HttpStatusCode status;
    private final String message;

    public CustomClientException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


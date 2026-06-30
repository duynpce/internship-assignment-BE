package org.example.authservice.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ExternalServiceException extends RuntimeException {
    private final HttpStatus status;
    public ExternalServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

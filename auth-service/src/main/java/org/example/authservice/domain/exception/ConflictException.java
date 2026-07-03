package org.example.authservice.domain.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}


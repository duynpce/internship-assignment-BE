package org.example.reportservice.infrastructure.web.exception;

import net.sf.jasperreports.engine.JRException;
import org.example.reportservice.domain.exception.ExternalServiceException;
import org.example.reportservice.infrastructure.web.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ResponseDto<Void>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ResponseDto.failure(message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ResponseDto<Void>> handleExternal(ExternalServiceException ex) {
        return build(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(JRException.class)
    public ResponseEntity<ResponseDto<Void>> handleJRException(JRException ex) {
        ex.printStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while generating the report.");
    }
}

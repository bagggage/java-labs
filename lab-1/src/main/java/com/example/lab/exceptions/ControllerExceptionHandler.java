package com.example.lab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static ResponseEntity<ExceptionResponse> buildResponseEntity(String message,
                                                                        HttpStatus status) {
        return new ResponseEntity<>(new ExceptionResponse(message, status), status);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<ExceptionResponse> handleNotFound(NotFoundException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { InvalidParamsException.class, NotImplementedException.class })
    public ResponseEntity<ExceptionResponse> handleBadRequest(InvalidParamsException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UndoneException.class })
    public ResponseEntity<ExceptionResponse> handleNotModified(UndoneException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<ExceptionResponse> handleInternalServerError(RuntimeException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

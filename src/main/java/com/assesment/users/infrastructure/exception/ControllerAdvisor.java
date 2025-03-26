package com.assesment.users.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> userDoesNotExistException(UserDoesNotExistException e){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, 404, List.of(e.getMessage()));
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> incorrectPasswordException(IncorrectPasswordException e){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, 401, List.of(e.getMessage()));
        return ResponseEntity.status(401).body(errorResponse);
    }



}

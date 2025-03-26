package com.assesment.users.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private LocalDateTime dateTime;
    private HttpStatus status;
    private Integer statusCode;
    private List<String> errorMessages;

}

package com.mom.storefront_panoply.exception;

import com.mom.storefront_panoply.exception.service.ExceptionLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionLogService exceptionLogService;

    public GlobalExceptionHandler(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, HttpServletRequest request) {
        exceptionLogService.save(ex, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }
}

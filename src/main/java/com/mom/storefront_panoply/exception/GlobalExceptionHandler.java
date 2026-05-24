package com.mom.storefront_panoply.exception;

import com.mom.storefront_panoply.exception.service.ExceptionLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionLogService exceptionLogService;

    public GlobalExceptionHandler(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(Throwable ex,
                                         HttpServletRequest request) {

        try {
            exceptionLogService.save(ex, request);
        } catch (Exception ignored) {
            throw ignored;
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }
}

package com.mom.storefront_panoply.exception;

import com.mom.storefront_panoply.exception.service.ExceptionLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ExceptionLogService exceptionLogService;

    public GlobalExceptionFilter(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (Throwable ex) {

            try {
                exceptionLogService.save(ex, request);
            } catch (Exception ignored) {
            }

            throw ex;
        }
    }
}
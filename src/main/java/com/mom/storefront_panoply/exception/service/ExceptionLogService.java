package com.mom.storefront_panoply.exception.service;

import com.mom.storefront_panoply.exception.ExceptionLog;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExceptionLogService {
    private MongoTemplate mongoTemplate;

    public void save(Exception ex, HttpServletRequest request) {
        ExceptionLog log = new ExceptionLog();
        log.setOccurredAt(LocalDateTime.now());
        log.setExceptionType(ex.getClass().getName());
        log.setMessage(ex.getMessage());
        log.setStackTrace(getStackTraceAsString(ex));

        if (request != null) {
            log.setPath(request.getRequestURI());
            log.setHttpMethod(request.getMethod());
        }
    }

    private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public List<ExceptionLog> getExceptionLogs() {
        return mongoTemplate.findAll(ExceptionLog.class);
    }

}

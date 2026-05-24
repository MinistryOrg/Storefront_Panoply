package com.mom.storefront_panoply.exception.service;

import com.mom.storefront_panoply.exception.ExceptionLog;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExceptionLogService {
    private final MongoTemplate mongoTemplate;

    public void save(Throwable ex, HttpServletRequest request) {

        try {

            ExceptionLog log = new ExceptionLog();

            log.setMessage(ex.getMessage());
            log.setStackTrace(ExceptionUtils.getStackTrace(ex));
            log.setPath(request.getRequestURI());
            log.setMethod(request.getMethod());

            mongoTemplate.save(log);

        } catch (Exception e) {
            ExceptionLog log = new ExceptionLog();

            log.setMessage(ex.getMessage());
            log.setStackTrace(ExceptionUtils.getStackTrace(ex));
            log.setPath(request.getRequestURI());
            log.setMethod(request.getMethod());

            mongoTemplate.save(log);
            e.printStackTrace();
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

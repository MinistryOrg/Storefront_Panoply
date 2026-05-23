package com.mom.storefront_panoply.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "exception_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionLog {

    @Id
    private Long id;

    private LocalDateTime occurredAt;

    private String exceptionType;

    private String message;

    private String stackTrace;

    private String path;

    private String httpMethod;

    private boolean emailed = false;

    // getters and setters
}

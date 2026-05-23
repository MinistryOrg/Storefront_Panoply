package com.mom.storefront_panoply.exception.controller;

import com.mom.storefront_panoply.exception.ExceptionLog;
import com.mom.storefront_panoply.exception.service.ExceptionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("panoply")
@RequiredArgsConstructor
public class ExceptionLogController {
    private ExceptionLogService exceptionLogService;

    @GetMapping("logs")
    public List<ExceptionLog> getExceptionLogs(){
        return exceptionLogService.getExceptionLogs();
    }
}

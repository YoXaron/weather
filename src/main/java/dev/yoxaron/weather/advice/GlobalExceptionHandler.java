package dev.yoxaron.weather.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public String handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error", e);
        return "error";
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public String handleDataIntegrityViolationException(Exception e, HttpServletRequest request) {
        log.error("DataIntegrityViolationException", e);
        return "redirect:/home";
    }
}

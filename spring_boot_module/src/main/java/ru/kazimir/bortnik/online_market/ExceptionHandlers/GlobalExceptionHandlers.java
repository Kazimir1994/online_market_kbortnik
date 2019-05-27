package ru.kazimir.bortnik.online_market.ExceptionHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandlers {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlers.class);

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final void typeDefinitionError(Exception ex) {
        logger.info("Error:= {} ", ex.getMessage());
    }
}

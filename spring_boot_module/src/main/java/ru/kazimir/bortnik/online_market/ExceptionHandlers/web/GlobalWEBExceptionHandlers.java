package ru.kazimir.bortnik.online_market.ExceptionHandlers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_500;

@ControllerAdvice(basePackages = "ru.kazimir.bortnik.online_market.controllers.web")
public class GlobalWEBExceptionHandlers {
    private final static Logger logger = LoggerFactory.getLogger(GlobalWEBExceptionHandlers.class);

    @ExceptionHandler(NumberFormatException.class)
    public final String typeDefinitionError(Exception ex) {
        logger.info("Error:= {} ", ex.getMessage());
        return REDIRECT_ERROR_500;
    }
}


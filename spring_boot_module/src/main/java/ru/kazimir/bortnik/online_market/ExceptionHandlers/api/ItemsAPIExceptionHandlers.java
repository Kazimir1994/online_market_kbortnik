package ru.kazimir.bortnik.online_market.ExceptionHandlers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.kazimir.bortnik.online_market.controllers.api.ItemAPIController;
import ru.kazimir.bortnik.online_market.service.exception.ItemServiceException;

@ControllerAdvice(assignableTypes = ItemAPIController.class)
public class ItemsAPIExceptionHandlers {
    private final static Logger logger = LoggerFactory.getLogger(ItemsAPIExceptionHandlers.class);

    @ExceptionHandler(ItemServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final void notExistentHandler(Exception ex) {
        logger.info("Error:= {} ", ex.getMessage());
    }
}

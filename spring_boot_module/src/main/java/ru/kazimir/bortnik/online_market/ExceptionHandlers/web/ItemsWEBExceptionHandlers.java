package ru.kazimir.bortnik.online_market.ExceptionHandlers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kazimir.bortnik.online_market.controllers.web.customer.CustomerItemsWebController;
import ru.kazimir.bortnik.online_market.service.exception.ItemServiceException;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_404;

@ControllerAdvice(assignableTypes = {CustomerItemsWebController.class})
public class ItemsWEBExceptionHandlers {
    private final static Logger logger = LoggerFactory.getLogger(ItemsWEBExceptionHandlers.class);

    @ExceptionHandler(ItemServiceException.class)
    public final String notExistentHandler(Exception ex) {
        logger.info("Error:= {} ", ex.getMessage());
        return REDIRECT_ERROR_404;
    }
}

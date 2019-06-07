package ru.kazimir.bortnik.online_market.ExceptionHandlers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.kazimir.bortnik.online_market.controllers.api.OrdersAPIController;
import ru.kazimir.bortnik.online_market.service.exception.ItemServiceException;
import ru.kazimir.bortnik.online_market.service.exception.OrderServiceException;
import ru.kazimir.bortnik.online_market.service.exception.UserServiceException;

@ControllerAdvice(assignableTypes = OrdersAPIController.class)
public class OrderAPIExceptionsHandlers {
    private final static Logger logger = LoggerFactory.getLogger(OrderAPIExceptionsHandlers.class);

    @ExceptionHandler({ItemServiceException.class, OrderServiceException.class, UserServiceException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final void notExistentHandler(Exception ex) {
        logger.info("Error:= {} ", ex.getMessage());
    }
}

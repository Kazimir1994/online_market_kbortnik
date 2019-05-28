package ru.kazimir.bortnik.online_market.ExceptionHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.kazimir.bortnik.online_market.controllers.api.ArticlesAPIController;
import ru.kazimir.bortnik.online_market.service.exception.ArticleServiceException;

@ControllerAdvice(assignableTypes = ArticlesAPIController.class)
public class ArticlesAPIExceptionHandlers {
    private final static Logger logger = LoggerFactory.getLogger(ArticlesAPIExceptionHandlers.class);

    @ExceptionHandler(ArticleServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final void notExistentHandler(Exception ex) {
        logger.info("Error:= {} ", ex.getMessage());
    }

}

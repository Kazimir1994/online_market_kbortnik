package ru.kazimir.bortnik.online_market.service.exception;

public class ArticleServiceException extends RuntimeException {
    public ArticleServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ArticleServiceException(String message) {
        super(message);
    }

    public ArticleServiceException() {
    }
}

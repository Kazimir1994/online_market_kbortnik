package ru.kazimir.bortnik.online_market.service.exception;

public class ReviewServiceException extends RuntimeException {
    public ReviewServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ReviewServiceException(String message) {
        super(message);
    }
}

package ru.kazimir.bortnik.online_market.service.exception;

public class ItemServiceException extends RuntimeException {

    public ItemServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ItemServiceException(String message) {
        super(message);
    }

    public ItemServiceException() {

    }
}
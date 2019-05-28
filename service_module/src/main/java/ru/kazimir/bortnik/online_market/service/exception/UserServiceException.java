package ru.kazimir.bortnik.online_market.service.exception;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException() {

    }
}
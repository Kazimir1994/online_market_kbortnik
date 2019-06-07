package ru.kazimir.bortnik.online_market.service.exception;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException() {
    }
}
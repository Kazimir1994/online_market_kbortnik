package ru.kazimir.bortnik.online_market.service.exception;

public class RoleServiceException extends RuntimeException {
    public RoleServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RoleServiceException(String message) {
        super(message);
    }
}

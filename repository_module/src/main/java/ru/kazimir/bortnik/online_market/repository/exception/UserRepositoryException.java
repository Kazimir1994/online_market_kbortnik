package ru.kazimir.bortnik.online_market.repository.exception;

public class UserRepositoryException extends RuntimeException {
    public UserRepositoryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UserRepositoryException(String message) {
        super(message);
    }
}

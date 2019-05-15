package ru.kazimir.bortnik.online_market.repository.exception;

public class RoleRepositoryException extends RuntimeException {
    public RoleRepositoryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RoleRepositoryException(String message) {
        super(message);
    }
}

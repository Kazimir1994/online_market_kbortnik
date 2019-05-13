package ru.kazimir.bortnik.online_market.repository.exception;

public class ReviewRepositoryException extends RuntimeException {
    public ReviewRepositoryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ReviewRepositoryException(String message) {
        super(message);
    }
}

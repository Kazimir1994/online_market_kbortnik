package ru.kazimir.bortnik.online_market.repository.exception;

public class ConnectionDataBaseExceptions extends RuntimeException {
    public ConnectionDataBaseExceptions(String message, Throwable throwable) {
        super(message, throwable);
    }
}

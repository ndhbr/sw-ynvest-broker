package de.ndhbr.ynvest.exception;

public class ServiceUnavailableException extends Exception {
    private final String message;

    public ServiceUnavailableException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

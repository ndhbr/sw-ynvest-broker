package de.ndhbr.ynvest.exception;

public class NotFoundException extends Exception {
    int code;
    String shortDescription;

    public NotFoundException(int code, String shortDescription) {
        this.code = code;
        this.shortDescription = shortDescription;
    }
}

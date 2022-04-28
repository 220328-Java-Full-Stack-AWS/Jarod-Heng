package com.revature.exceptions;

public class EmailNotUniqueException extends RuntimeException {

    public EmailNotUniqueException() {
        super("ERROR: Email already in use.");
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

    public EmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotUniqueException(Throwable cause) {
        super(cause);
    }

    public EmailNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

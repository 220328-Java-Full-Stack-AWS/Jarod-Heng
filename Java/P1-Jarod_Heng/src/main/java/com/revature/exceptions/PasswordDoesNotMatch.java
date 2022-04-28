package com.revature.exceptions;

public class PasswordDoesNotMatch extends RuntimeException {

    public PasswordDoesNotMatch() {
        super("ERROR: Password Does Not Match.");
    }

    public PasswordDoesNotMatch(String message) {
        super(message);
    }

    public PasswordDoesNotMatch(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordDoesNotMatch(Throwable cause) {
        super(cause);
    }

    public PasswordDoesNotMatch(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

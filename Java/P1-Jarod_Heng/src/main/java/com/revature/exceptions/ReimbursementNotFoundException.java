package com.revature.exceptions;

public class ReimbursementNotFoundException extends RuntimeException {

    public ReimbursementNotFoundException() {
        super("ERROR: Reimbursement not found");
    }

    public ReimbursementNotFoundException(String message) {
        super(message);
    }

    public ReimbursementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReimbursementNotFoundException(Throwable cause) {
        super(cause);
    }

    public ReimbursementNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.revature.exceptions;

public class ReimbursementUpdateFailedException extends RuntimeException {

    public ReimbursementUpdateFailedException() {
        super("ERROR: Reimbursement update failed.");
    }

    public ReimbursementUpdateFailedException(String message) {
        super(message);
    }

    public ReimbursementUpdateFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReimbursementUpdateFailedException(Throwable cause) {
        super(cause);
    }

    public ReimbursementUpdateFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.revature.exceptions;

public class RoleAccessDeniedException extends RuntimeException {

    public RoleAccessDeniedException() {
        super("Non finance manager user attempting to process a reimbursement.");
    }

    public RoleAccessDeniedException(String message) {
        super(message);
    }

    public RoleAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleAccessDeniedException(Throwable cause) {
        super(cause);
    }

    public RoleAccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

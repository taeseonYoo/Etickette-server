package com.tae.Etickette.member.application;

public class NoChangeablePermission extends RuntimeException {
    public NoChangeablePermission() {
        super();
    }

    public NoChangeablePermission(String message) {
        super(message);
    }

    public NoChangeablePermission(String message, Throwable cause) {
        super(message, cause);
    }

    public NoChangeablePermission(Throwable cause) {
        super(cause);
    }

    protected NoChangeablePermission(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

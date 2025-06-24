package com.tae.Etickette.booking.command.application;

public class NoCancellablePermission extends RuntimeException {
    public NoCancellablePermission() {
        super();
    }

    public NoCancellablePermission(String message) {
        super(message);
    }

    public NoCancellablePermission(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCancellablePermission(Throwable cause) {
        super(cause);
    }

    protected NoCancellablePermission(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

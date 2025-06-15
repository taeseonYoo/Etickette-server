package com.tae.Etickette.venue.application;

public class NoDeletablePermission extends RuntimeException {
    public NoDeletablePermission() {
        super();
    }

    public NoDeletablePermission(String message) {
        super(message);
    }

    public NoDeletablePermission(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDeletablePermission(Throwable cause) {
        super(cause);
    }

    protected NoDeletablePermission(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

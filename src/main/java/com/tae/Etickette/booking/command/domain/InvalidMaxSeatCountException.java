package com.tae.Etickette.booking.command.domain;

public class InvalidMaxSeatCountException extends RuntimeException {
    public InvalidMaxSeatCountException() {
        super();
    }

    public InvalidMaxSeatCountException(String message) {
        super(message);
    }

    public InvalidMaxSeatCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMaxSeatCountException(Throwable cause) {
        super(cause);
    }

    protected InvalidMaxSeatCountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

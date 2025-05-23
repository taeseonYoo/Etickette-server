package com.tae.Etickette.concert.domain;

public class VenueAlreadyDeletedException extends RuntimeException {
    public VenueAlreadyDeletedException() {
        super();
    }

    public VenueAlreadyDeletedException(String message) {
        super(message);
    }

    public VenueAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public VenueAlreadyDeletedException(Throwable cause) {
        super(cause);
    }

    protected VenueAlreadyDeletedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

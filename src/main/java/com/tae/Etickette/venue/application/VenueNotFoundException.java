package com.tae.Etickette.venue.application;

public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException() {
        super();
    }

    public VenueNotFoundException(String message) {
        super(message);
    }

    public VenueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VenueNotFoundException(Throwable cause) {
        super(cause);
    }

    protected VenueNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

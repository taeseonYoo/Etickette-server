package com.tae.Etickette.booking.application;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException() {
        super();
    }

    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BookingNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.tae.Etickette.booking.command.domain;

public class BookingCompletionFailedException extends RuntimeException {
    public BookingCompletionFailedException() {
        super();
    }

    public BookingCompletionFailedException(String message) {
        super(message);
    }

    public BookingCompletionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingCompletionFailedException(Throwable cause) {
        super(cause);
    }

    protected BookingCompletionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

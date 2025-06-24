package com.tae.Etickette.booking.command.application;

public class BookSeatNotFoundException extends RuntimeException {
    public BookSeatNotFoundException() {
    }

    public BookSeatNotFoundException(String message) {
        super(message);
    }

    public BookSeatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookSeatNotFoundException(Throwable cause) {
        super(cause);
    }

    public BookSeatNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

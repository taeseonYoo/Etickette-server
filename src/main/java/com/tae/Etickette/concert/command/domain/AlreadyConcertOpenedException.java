package com.tae.Etickette.concert.command.domain;

public class AlreadyConcertOpenedException extends RuntimeException {
    public AlreadyConcertOpenedException() {
        super();
    }

    public AlreadyConcertOpenedException(String message) {
        super(message);
    }

    public AlreadyConcertOpenedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyConcertOpenedException(Throwable cause) {
        super(cause);
    }

    protected AlreadyConcertOpenedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

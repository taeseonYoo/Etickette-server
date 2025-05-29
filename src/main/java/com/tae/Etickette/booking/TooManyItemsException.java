package com.tae.Etickette.booking;

public class TooManyItemsException extends RuntimeException {
    public TooManyItemsException() {
        super();
    }

    public TooManyItemsException(String message) {
        super(message);
    }

    public TooManyItemsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyItemsException(Throwable cause) {
        super(cause);
    }

    protected TooManyItemsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

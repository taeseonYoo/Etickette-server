package com.tae.Etickette.concert.application;

public class ScheduleDuplicateException extends RuntimeException {
    public ScheduleDuplicateException() {
    }

    public ScheduleDuplicateException(String message) {
        super(message);
    }

    public ScheduleDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleDuplicateException(Throwable cause) {
        super(cause);
    }

    public ScheduleDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

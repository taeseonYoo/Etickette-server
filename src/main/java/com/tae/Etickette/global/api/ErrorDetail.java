package com.tae.Etickette.global.api;

import lombok.Getter;

@Getter
public class ErrorDetail {
    private final int status;
    private final String code;
    private final String message;

    public ErrorDetail(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

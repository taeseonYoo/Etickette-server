package com.tae.Etickette.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;

    static ErrorResponse of(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getCode(), errorCode.getDescription());
    }
}

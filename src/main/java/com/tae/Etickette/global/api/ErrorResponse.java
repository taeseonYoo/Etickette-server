package com.tae.Etickette.global.api;

import com.tae.Etickette.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    @Schema(example = "false")
    private final boolean success = false;
    private final String code;
    private final String message;

    public static ErrorResponse of(ErrorCode errorCode, String message){
        return new ErrorResponse(errorCode.getCode(), message);
    }
    public static ErrorResponse of(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getCode(),errorCode.getDescription());
    }

    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

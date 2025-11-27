package com.tae.Etickette.global.api;

import com.tae.Etickette.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorDetail error;

    private ApiResponse(boolean success, T data, ErrorDetail error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode,String message) {
        return new ApiResponse<>(false, null, new ErrorDetail(errorCode.getStatus(), errorCode.getCode(), message));
    }

}

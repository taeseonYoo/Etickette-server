package com.tae.Etickette.global.api;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {
    private final boolean success;
    private final T data;

    private SuccessResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<>(true, data);
    }

}

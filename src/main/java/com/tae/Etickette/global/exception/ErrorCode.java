package com.tae.Etickette.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 서버 오류
     */
    INTERNAL_SERVER_ERROR(500,"COMMON-002" ,"서버 오류가 발생했습니다.");

    private final int status;
    private final String code;
    private final String description;
}

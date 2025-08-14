package com.tae.Etickette.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    //유저 오류
    USER_NOT_FOUND(404,"USER-001","회원 정보를 찾을 수 없는 경우"),
    DUPLICATE_EMAIL(400,"USER-002","이메일이 중복된 경우"),

    //서버 오류
    INTERNAL_SERVER_ERROR(500,"SERVER-001" ,"서버가 요청을 처리할 수 없는 경우"),

    // 인증 & 인가
    NO_PERMISSION(403,"AUTH-001","권한이 부족한 경우");

    private final int status;
    private final String code;
    private final String description;
}

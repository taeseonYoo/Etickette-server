package com.tae.Etickette.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    //유저 오류
    USER_NOT_FOUND(404,"USER-001","회원 정보를 찾을 수 없는 경우"),
    DUPLICATE_EMAIL(400,"USER-002","이메일이 중복된 경우"),
    PASSWORD_NOT_MATCH(400,"USER-003","비밀번호가 일치하지 않는 경우"),

    //공연장 오류
    VENUE_NOT_FOUND(404,"VENUE-001","공연장 정보를 찾을 수 없는 경우"),
    CAPACITY_CANNOT_BE_NEGATIVE(400,"VENUE-002","수용량이 0보다 작은 경우"),
    DUPLICATE_ADDRESS(400,"VENUE-003","주소가 중복되는 경우"),

    //서버 오류
    INTERNAL_SERVER_ERROR(500,"SERVER-001" ,"서버가 요청을 처리할 수 없는 경우"),

    // 인증 & 인가
    NO_PERMISSION(403,"AUTH-001","권한이 부족한 경우"),
    UNAUTHORIZED(401,"AUTH-002","인증되지 않은 경우");


    private final int status;
    private final String code;
    private final String description;
}

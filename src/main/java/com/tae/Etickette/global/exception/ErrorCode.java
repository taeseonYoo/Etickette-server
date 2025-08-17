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

    //공연 오류
    CONCERT_NOT_FOUND(404,"CONCERT-001","공연 정보를 찾을 수 없는 경우"),
    CONCERT_NOT_READY(409,"CONCERT-002","공연이 준비되지 않은 경우"),
    CONCERT_NOT_OPEN(409,"CONCERT-003","공연이 오픈되지 않은 경우"),
    CONCERT_NOT_CLOSED(409,"CONCERT-004","공연이 종료되지 않은 경우"),
    CONCERT_NOT_ACTIVATE(409,"CONCERT-005","공연이 활성화 되지 않은 경우"),

    //세션 오류
    SESSION_NOT_FOUND(404,"SESSION-001","세션 정보를 찾을 수 없는 경우"),
    SESSION_NOT_CLOSED(409,"SESSION-002","세션이 이미 취소된 경우"),
    DUPLICATE_DATE(409, "SESSION-003", "해당 스케줄이 이미 존재하여 세션을 등록할 수 없는 경우"),

    //예매 오류
    BOOKING_NOT_FOUND(404,"BOOKING-001","예매 정보를 찾을 수 없는 경우"),


    //좌석 오류
    SEAT_NOT_FOUND(404,"SEAT-001","좌석을 찾을 수 없는 경우"),

    //예약 좌석 오류
    BOOKSEAT_NOT_FOUND(404,"BOOKSEAT-001","예약 좌석을 찾을 수 없는 경우"),
    BOOKSEAT_NOT_LOCKED(409,"BOOKSEAT-002","예약 좌석이 예매가 불가능한 경우"),
    BOOKSEAT_CANNOT_BE_CANCELED(409,"BOOKSEAT-003","예약 좌석이 취소가 불가능한 경우"),
    BOOKSEAT_ALREADY_RESERVED(409,"BOOKSEAT-004","예약 좌석이 이미 예약된 경우"),

    //이미지 업로드
    S3_UPLOAD_ERROR(500,"IMAGE-001","이미지 업로드 중 오류가 발생한 경우"),

    //서버 오류
    INTERNAL_SERVER_ERROR(500,"SERVER-001" ,"서버가 요청을 처리할 수 없는 경우"),

    // 인증 & 인가
    NO_PERMISSION(403,"AUTH-001","권한이 부족한 경우"),
    UNAUTHORIZED(401,"AUTH-002","인증되지 않은 경우");


    private final int status;
    private final String code;
    private final String description;
}

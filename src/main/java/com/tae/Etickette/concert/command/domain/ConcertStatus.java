package com.tae.Etickette.concert.command.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConcertStatus {
    READY("READY"), //공연 등록
    OPEN("OPEN"), //예매 가능 상태
    CLOSED("CLOSED"), //예매 마감
    CANCELED("CANCELED"), //공연 취소
    FINISHED("FINISHED"); //공연 종료
    private final String value;
}

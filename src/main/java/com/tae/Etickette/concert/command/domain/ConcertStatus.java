package com.tae.Etickette.concert.command.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConcertStatus {
    BEFORE("BEFORE"), //공연 등록
    OPEN("OPEN"), //예매 가능 상태
    COMPLETED("COMPLETED"), //티켓팅 종료
    CANCELED("CANCELED"); //공연 취소
    private final String value;
}

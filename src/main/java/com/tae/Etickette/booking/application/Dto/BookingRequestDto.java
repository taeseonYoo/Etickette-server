package com.tae.Etickette.booking.application.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class BookingRequestDto {
    private Long sessionId;
    private Long memberId;

    private List<SeatInfo> seatInfos;

    @Builder
    public BookingRequestDto(Long sessionId, Long memberId, List<SeatInfo> seatInfos) {
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.seatInfos = seatInfos;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeatInfo {
        private String row;
        private Integer column;
    }
}

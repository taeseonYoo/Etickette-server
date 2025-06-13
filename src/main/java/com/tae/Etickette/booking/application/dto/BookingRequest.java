package com.tae.Etickette.booking.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BookingRequest {
    private Long sessionId;
    private Long memberId;
    private List<Long> seatIds;

    @Builder
    public BookingRequest(Long sessionId, Long memberId, List<Long> seatIds) {
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.seatIds = seatIds;
    }
}

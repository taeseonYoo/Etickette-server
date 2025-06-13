package com.tae.Etickette.booking.application.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

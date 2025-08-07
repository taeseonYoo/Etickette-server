package com.tae.Etickette.booking.command.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookingRequest {
    private Long sessionId;
    private List<Long> seatIds;

    @Builder
    public BookingRequest(Long sessionId,  List<Long> seatIds) {
        this.sessionId = sessionId;
        this.seatIds = new ArrayList<>(seatIds);
    }
}

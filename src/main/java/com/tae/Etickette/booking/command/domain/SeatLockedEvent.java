package com.tae.Etickette.booking.command.domain;

import java.util.List;
import lombok.Getter;

@Getter
public class SeatLockedEvent {
    private final List<Long> seatIds;
    private final Long sessionId;

    public SeatLockedEvent(List<Long> seatIds, Long sessionId) {
        this.seatIds = seatIds;
        this.sessionId = sessionId;
    }
}

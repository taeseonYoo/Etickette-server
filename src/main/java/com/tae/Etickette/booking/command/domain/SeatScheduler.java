package com.tae.Etickette.booking.command.domain;

import java.util.List;

public interface SeatScheduler {
    void scheduling(List<Long> seatIds, Long sessionId);
}

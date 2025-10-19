package com.tae.Etickette.booking.infra;

import com.tae.Etickette.booking.command.domain.SeatLockedEvent;
import com.tae.Etickette.booking.command.domain.SeatScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SeatLockedEventHandler {
    private final SeatScheduler seatScheduler;

    public SeatLockedEventHandler(SeatScheduler seatScheduler) {
        this.seatScheduler = seatScheduler;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SeatLockedEvent event) {
        seatScheduler.scheduling(event.getSeatIds(), event.getSessionId());
    }
}

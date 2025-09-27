package com.tae.Etickette.concert.infra;

import com.tae.Etickette.concert.command.domain.ConcertCreatedEvent;
import com.tae.Etickette.seat.application.RegisterSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertCreatedEventHandler {
    private final RegisterSeatService registerSeatService;

    @EventListener(ConcertCreatedEvent.class)
    public void handle(ConcertCreatedEvent event) {
        registerSeatService.register(event.getSavedConcertId());
    }
}

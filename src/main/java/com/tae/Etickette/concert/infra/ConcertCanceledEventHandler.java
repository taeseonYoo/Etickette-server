package com.tae.Etickette.concert.infra;

import com.tae.Etickette.concert.command.domain.ConcertCanceledEvent;
import com.tae.Etickette.session.application.CancelSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertCanceledEventHandler {
    private final CancelSessionService cancelSessionService;

    @EventListener(ConcertCanceledEvent.class)
    public void handle(ConcertCanceledEvent event) {
        cancelSessionService.cancelByConcertId(event.getConcertId());
    }
}

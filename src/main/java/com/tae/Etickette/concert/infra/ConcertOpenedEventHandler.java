package com.tae.Etickette.concert.infra;

import com.tae.Etickette.concert.command.domain.ConcertOpenedEvent;
import com.tae.Etickette.session.application.ManagementSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertOpenedEventHandler {
    private final ManagementSessionService managementSessionService;

    @EventListener(ConcertOpenedEvent.class)
    public void handle(ConcertOpenedEvent event) {
        managementSessionService.open(event.getConcertId());
    }
}

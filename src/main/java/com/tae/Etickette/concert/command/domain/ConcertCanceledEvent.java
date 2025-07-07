package com.tae.Etickette.concert.command.domain;

import com.tae.Etickette.global.event.Event;
import lombok.Getter;

@Getter
public class ConcertCanceledEvent extends Event {
    private Long concertId;

    public ConcertCanceledEvent(Long concertId) {
        super();
        this.concertId = concertId;
    }
}

package com.tae.Etickette.concert.command.domain;

import lombok.Getter;

@Getter
public class ConcertCreatedEvent {
    private Long savedConcertId;

    public ConcertCreatedEvent(Long savedConcertId) {
        this.savedConcertId = savedConcertId;
    }
}

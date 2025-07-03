package com.tae.Etickette.concert.command.domain;

import com.tae.Etickette.global.event.Event;
import lombok.Getter;

/**
 * 공연이 오픈되면 스케줄도 오픈한다.
 */
@Getter
public class ConcertOpenedEvent extends Event {
    private Long concertId;

    public ConcertOpenedEvent(Long concertId) {
        super();
        this.concertId = concertId;
    }
}

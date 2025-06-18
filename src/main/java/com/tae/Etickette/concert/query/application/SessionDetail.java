package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SessionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class SessionDetail {
    private Long sessionId;
    private LocalDate concertDate;
    private LocalTime startTime;

    private SessionStatus status;

    public SessionDetail(Session session) {
        this.sessionId = session.getId();
        this.concertDate = session.getConcertDate();
        this.startTime = session.getStartTime();
        this.status = session.getStatus();
    }
}

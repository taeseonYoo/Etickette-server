package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class SessionDetail {
    @Schema(description = "스케줄Id",example = "1")
    private final Long sessionId;
    @Schema(description = "콘서트 날짜",example = "2025-11-30")
    private final LocalDate concertDate;
    @Schema(type = "string",example = "10:00")
    private final LocalTime startTime;
    @Schema(description = "스케줄 상태")
    private final SessionStatus status;

    public SessionDetail(Session session) {
        this.sessionId = session.getId();
        this.concertDate = session.getConcertDate();
        this.startTime = session.getStartTime();
        this.status = session.getStatus();
    }
}

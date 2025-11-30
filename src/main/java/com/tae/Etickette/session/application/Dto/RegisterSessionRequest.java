package com.tae.Etickette.session.application.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class RegisterSessionRequest {
    @NotEmpty
    private final Long concertId;
    @NotEmpty
    private final List<SessionInfo> sessionInfos;

    @Builder
    public RegisterSessionRequest(Long concertId, List<SessionInfo> sessionInfos) {
        this.concertId = concertId;
        this.sessionInfos = sessionInfos;
    }

    @JsonIgnore
    public List<LocalDate> getConcertDates() {
        return sessionInfos.stream().map(SessionInfo::getConcertDate).toList();
    }

    @Getter
    public static class SessionInfo {
        @Schema(example = "2025-11-30")
        private final LocalDate concertDate;
        @Schema(type = "string",example = "10:00")
        private final LocalTime startTime;

        @Builder
        public SessionInfo(LocalDate concertDate, LocalTime startTime) {
            this.concertDate = concertDate;
            this.startTime = startTime;
        }
    }
}

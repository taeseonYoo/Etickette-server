package com.tae.Etickette.session.domain;

import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.exception.ConflictException;
import com.tae.Etickette.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column
    private LocalDate concertDate;
    @Column
    private LocalTime startTime;
    @Column
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
    /**
     * 연관관계
     */
    @Column
    private Long concertId;


    private Session(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId) {
        this.concertDate = concertDate;
        this.startTime = startTime;
        calculateEndTime(startTime, runningTime);
        this.status = SessionStatus.AVAILABLE;
        this.concertId = concertId;
    }

    public static Session create(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId) {
        return new Session(concertDate, startTime, runningTime, concertId);
    }

    //공연 종료시간을 계산한다.
    private void calculateEndTime(LocalTime startTime, Integer runningTime) {
        this.endTime = startTime.plusMinutes(runningTime);
    }

    public void cancel() {
        verifyCanceled();
        this.status = SessionStatus.CANCELED;
        Events.raise(new SessionCanceledEvent(this.getId()));
    }

    private void verifyCanceled() {
        if (this.status == SessionStatus.CANCELED)
            throw new ConflictException(ErrorCode.SESSION_NOT_CLOSED, "이미 취소 된 공연입니다.");
    }


}

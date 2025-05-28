package com.tae.Etickette.schedule.domain;

import com.tae.Etickette.concert.domain.Concert;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.support.SessionStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;
    @Column
    private LocalDate concertDate;
    @Column
    private LocalTime startTime;
    @Column
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;
    /**
     * 연관관계
     */
    private Long concertId;

    private Schedule(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId) {
        this.concertDate = concertDate;
        this.startTime = startTime;

        calculateEndTime(startTime, runningTime);

        this.status = ScheduleStatus.BEFORE;
        this.concertId = concertId;
    }

    public static Schedule create(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId) {
        return new Schedule(concertDate, startTime, runningTime, concertId);
    }

    private void calculateEndTime(LocalTime startTime, Integer runningTime) {
        this.endTime = startTime.plusMinutes(runningTime);
    }


}

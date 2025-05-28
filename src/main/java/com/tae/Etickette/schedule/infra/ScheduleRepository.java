package com.tae.Etickette.schedule.infra;

import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    @Query("""
        SELECT s FROM Schedule s
        JOIN s.concert p
        WHERE p.venue = :venue
          AND s.concertDate = :date
          AND s.startTime < :newEnd
          AND s.endTime > :newStart
    """)
    List<Schedule> findConflictingSchedules(
            @Param("venue") Venue venue,
            @Param("date") LocalDate date,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd
    );
}

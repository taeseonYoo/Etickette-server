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
JOIN Concert c ON s.concertId = c.id
WHERE s.concertDate = :date
AND c.venueId = :venueId
""")
    List<Schedule> findByConcertDateAndVenueId(
            @Param("date") LocalDate date,
            @Param("venueId") Long venueId
    );
}

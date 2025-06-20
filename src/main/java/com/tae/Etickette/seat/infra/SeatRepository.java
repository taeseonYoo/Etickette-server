package com.tae.Etickette.seat.infra;

import com.tae.Etickette.seat.domain.CustomSeatRepository;
import com.tae.Etickette.seat.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long>, CustomSeatRepository {
    @Query("SELECT s.id FROM Seat s WHERE s.concertId = :concertId")
    List<Long> findIdByConcertId(Long concertId);
}

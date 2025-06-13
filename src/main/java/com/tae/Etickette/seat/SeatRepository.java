package com.tae.Etickette.seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("SELECT s.id FROM Seat s WHERE s.concertId = :concertId")
    List<Long> findIdByConcertId(Long concertId);
}

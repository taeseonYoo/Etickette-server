package com.tae.Etickette.seat.query;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface SeatDataDao extends Repository<SeatData,Long> {
    Optional<SeatData> findById(Long seatId);
}

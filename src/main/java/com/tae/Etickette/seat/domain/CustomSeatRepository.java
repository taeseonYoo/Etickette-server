package com.tae.Etickette.seat.domain;


import java.util.List;

public interface CustomSeatRepository {
    void saveAllInBulk(List<Seat> seats);
}

package com.tae.Etickette.seat.application;

import com.tae.Etickette.seat.domain.Seat;
import com.tae.Etickette.seat.infra.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterSeatService {
    private final SeatRepository seatRepository;

    @Transactional
    public void register(Long concertId) {
        List<Seat> seats = initSeat(concertId);
        seatRepository.saveAllInBulk(seats);
    }
    private List<Seat> initSeat(Long concertId) {
        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('A' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('F' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('K' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        return seats;
    }
}

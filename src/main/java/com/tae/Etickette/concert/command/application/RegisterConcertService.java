package com.tae.Etickette.concert.command.application;

import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.command.domain.GradePrice;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.seat.domain.Seat;
import com.tae.Etickette.seat.infra.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterConcertService {
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public Long register(RegisterConcertRequest requestDto) {

        List<GradePrice> gradePrices = requestDto.getGradePrices().stream()
                .map(info -> new GradePrice(info.getGrade(), new Money(info.getPrice())))
                .toList();

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgURL(),
                gradePrices
        );
        Concert savedConcert = concertRepository.save(concert);

        List<Seat> seats = initSeat(savedConcert.getId());
        seatRepository.saveAllInBulk(seats);

        return savedConcert.getId();
    }

    private List<Seat> initSeat(Long concertId) {
        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('A' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('F' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        return seats;
    }
}

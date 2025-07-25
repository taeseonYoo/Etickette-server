package com.tae.Etickette.concert.command.application;

import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.command.domain.Image;
import com.tae.Etickette.concert.command.domain.ImageUploader;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.command.domain.GradePrice;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.seat.domain.Seat;
import com.tae.Etickette.seat.infra.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterConcertService {
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final ImageUploader imageUploader;
    @Transactional
    public RegisterConcertResponse register(RegisterConcertRequest requestDto, MultipartFile imageFile){
        //가격 정보
        List<GradePrice> gradePrices = requestDto.getGradePrices().stream()
                .map(info -> new GradePrice(info.getGrade(), new Money(info.getPrice())))
                .toList();

        //이미지 업로드
        String uploadedURL = imageUploader.upload(imageFile);

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                new Image(uploadedURL),
                gradePrices,
                requestDto.getVenueId()
        );

        Concert savedConcert = concertRepository.save(concert);

        List<Seat> seats = initSeat(savedConcert.getId());
        seatRepository.saveAllInBulk(seats);

        return RegisterConcertResponse.builder().concertId(concert.getId()).build();
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

package com.tae.Etickette.integration.service;

import com.tae.Etickette.booking.application.Dto.BookingRequestDto;
import com.tae.Etickette.booking.application.BookingService;
import com.tae.Etickette.session.application.Dto.RegisterSessionReqDto;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.tae.Etickette.booking.application.Dto.BookingRequestDto.*;
import static com.tae.Etickette.session.application.Dto.RegisterSessionReqDto.*;


@SpringBootTest
@Transactional
class BookingServiceTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private RegisterSessionService registerSessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    public void setUp() {
        List<SessionInfo> sessionInfos = List.of(SessionInfo.builder()
                .concertDate(LocalDate.of(2025, 6, 1)).startTime(LocalTime.of(15, 0))
                .build());
        RegisterSessionReqDto requestDto = new RegisterSessionReqDto(1L, 1L, sessionInfos);
        registerSessionService.register(requestDto);
    }

    @Test
    void 예약_성공() {
        //given
        List<SeatInfo> requestSeats = List.of(new SeatInfo("A", 1), new SeatInfo("E", 10));

        BookingRequestDto requestDto = BookingRequestDto.builder()
                .memberId(1L)
                .sessionId(1L)
                .seatInfos(requestSeats)
                .build();

        //when
        Long sessionId = bookingService.booking(requestDto);

        //then
        Session session = sessionRepository.findById(sessionId).get();


    }

}
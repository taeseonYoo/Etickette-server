package com.tae.Etickette.integration.service;

import com.tae.Etickette.booking.application.Dto.BookingRequestDto;
import com.tae.Etickette.booking.application.BookingService;
import com.tae.Etickette.session.application.Dto.SessionRegisterReqDto;
import com.tae.Etickette.session.application.SessionRegisterService;
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


@SpringBootTest
@Transactional
class BookingServiceTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private SessionRegisterService sessionRegisterService;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    public void setUp() {
        SessionRegisterReqDto requestDto = new SessionRegisterReqDto(LocalDate.of(2025, 6, 1), LocalTime.of(14, 0), 120, 1L, 1L);
        sessionRegisterService.registerSession(requestDto);
    }

    @Test
    void 예약_성공() {
        //given
        List<SeatInfo> requestSeats = List.of(new SeatInfo("A", 1), new SeatInfo("E", 10));

        BookingRequestDto requestDto = builder()
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
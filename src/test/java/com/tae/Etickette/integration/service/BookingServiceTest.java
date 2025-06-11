package com.tae.Etickette.integration.service;

import com.tae.Etickette.booking.application.Dto.BookingRequestDto;
import com.tae.Etickette.booking.application.BookingService;
import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.concert.ConcertRegisterReqDto;
import com.tae.Etickette.concert.ConcertRegisterService;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.session.application.Dto.RegisterSessionReqDto;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.venue.application.Dto.VenueCreateResponseDto;
import com.tae.Etickette.venue.application.RegisterVenueService;
import com.tae.Etickette.venue.domain.Venue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.tae.Etickette.booking.application.Dto.BookingRequestDto.*;
import static com.tae.Etickette.concert.ConcertRegisterReqDto.*;
import static com.tae.Etickette.session.application.Dto.RegisterSessionReqDto.*;


@SpringBootTest
@Transactional
class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RegisterSessionService registerSessionService;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RegisterVenueService registerVenueService;
    @Autowired
    private ConcertRegisterService concertRegisterService;

    @BeforeEach
    public void setUp() {
        //공연장 등록
        VenueCreateRequestDto venueDto = createVenue();
        VenueCreateResponseDto register = registerVenueService.register(venueDto);

        List<GradePriceInfo> gradePriceInfos = List.of(
                GradePriceInfo.builder().grade("VIP").price(150000).build(),
                GradePriceInfo.builder().grade("S").price(100000).build()
        );
        //공연 등록
        ConcertRegisterReqDto concertDto = createConcert(gradePriceInfos);
        concertRegisterService.registerConcert(concertDto);

        //공연 일정 등록
        List<SessionInfo> sessionInfos = List.of(
                SessionInfo.builder()
                        .concertDate(LocalDate.of(2025, 6, 1))
                        .startTime(LocalTime.of(15, 0)).build(),
                SessionInfo.builder()
                        .concertDate(LocalDate.of(2025, 6, 2))
                        .startTime(LocalTime.of(15, 0)).build()
        );
        RegisterSessionReqDto sessionDto = sessionCreate(sessionInfos);
        registerSessionService.register(sessionDto);
    }

    @Test
    void 예약_성공() {
        //given
        List<SeatInfo> requestSeats = List.of(new SeatInfo("A", 1), new SeatInfo("F", 10));

        BookingRequestDto requestDto = BookingRequestDto.builder()
                .memberId(1L)
                .sessionId(1L)
                .seatInfos(requestSeats)
                .build();

        //when
        BookingRef bookingRef = bookingService.booking(requestDto);

        //then
        Booking booking = bookingRepository.findById(bookingRef).get();
        System.out.println(booking.getTotalAmounts().getValue());
    }

    private VenueCreateRequestDto createVenue() {
        return VenueCreateRequestDto.builder()
                .place("KSPO")
                .capacity(10000)
                .address(new Address("서울시", "송파구", "11111")).build();
    }

    private ConcertRegisterReqDto createConcert(List<GradePriceInfo> gradePriceInfos) {
        return ConcertRegisterReqDto.builder().title("IU HER").overview("아이유 공연")
                .runningTime(120).imgURL("")
                .gradePrices(
                        gradePriceInfos
                )
                .build();
    }

    private RegisterSessionReqDto sessionCreate(List<SessionInfo> sessionInfos) {
        return RegisterSessionReqDto.builder().venueId(1L)
                .concertId(1L)
                .sessionInfos(sessionInfos).build();
    }
}
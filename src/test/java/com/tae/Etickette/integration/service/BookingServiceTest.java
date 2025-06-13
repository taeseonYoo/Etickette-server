package com.tae.Etickette.integration.service;

import com.tae.Etickette.booking.application.Dto.BookingRequest;
import com.tae.Etickette.booking.application.BookingService;
import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.concert.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.application.ConcertRegisterService;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.venue.application.Dto.VenueCreateRequest;
import com.tae.Etickette.venue.application.Dto.VenueCreateResponse;
import com.tae.Etickette.venue.application.RegisterVenueService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.tae.Etickette.booking.application.Dto.BookingRequest.*;
import static com.tae.Etickette.concert.application.dto.RegisterConcertRequest.*;
import static com.tae.Etickette.session.application.Dto.RegisterSessionRequest.*;


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


    List<Long> sessionIds;
    @BeforeEach
    public void setUp() {
        //공연장 등록
        VenueCreateRequest venueDto = VenueCreateBuilder.builder().build();
        VenueCreateResponse register = registerVenueService.register(venueDto);

        List<GradePriceInfo> gradePriceInfos = List.of(
                GradePriceInfo.builder().grade("VIP").price(150000).build(),
                GradePriceInfo.builder().grade("S").price(100000).build(),
                GradePriceInfo.builder().grade("R").price(50000).build()
        );
        //공연 등록
        RegisterConcertRequest concertDto = ConcertCreateBuilder.builder().gradePrices(gradePriceInfos).build();
        Long concertId = concertRegisterService.register(concertDto);

        //공연 일정 등록
        List<SessionInfo> sessionInfos = List.of(
                SessionInfo.builder()
                        .concertDate(LocalDate.of(2025, 6, 1))
                        .startTime(LocalTime.of(15, 0)).build(),
                SessionInfo.builder()
                        .concertDate(LocalDate.of(2025, 6, 2))
                        .startTime(LocalTime.of(15, 0)).build()
        );

        RegisterSessionRequest sessionDto = RegisterSessionRequest.builder()
                .venueId(register.getId())
                .concertId(concertId)
                .sessionInfos(sessionInfos)
                .build();
        sessionIds = registerSessionService.register(sessionDto);
    }

    @Test
    @DisplayName("예약에 성공한다.")
    void 예약_성공() {
//        given
        List<Long> seatIds = List.of(1L,2L);

        BookingRequest requestDto = BookingRequest.builder()
                .memberId(1L)
                .sessionId(sessionIds.get(0))
                .seatIds(seatIds)
                .build();

        //when
        BookingRef bookingRef = bookingService.booking(requestDto);

        //then
        Booking booking = bookingRepository.findById(bookingRef).get();
        Assertions.assertThat(booking.getTotalAmounts().getValue()).isEqualTo(150000 + 150000);
    }

}
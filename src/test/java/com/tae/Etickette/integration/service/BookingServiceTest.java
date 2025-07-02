package com.tae.Etickette.integration.service;

import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.booking.command.application.BookingService;
import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.seat.infra.SeatRepository;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest.*;
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
    private RegisterConcertService registerConcertService;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private MemberRepository memberRepository;

    List<Long> sessionIds;
    List<Long> idByConcertId;
    @BeforeEach
    public void setUp() {
        //공연장 등록
        RegisterVenueRequest venueDto = VenueCreateBuilder.builder().build();
        RegisterVenueResponse venue = registerVenueService.register(venueDto);

        List<GradePriceInfo> gradePriceInfos = List.of(
                GradePriceInfo.builder().grade("VIP").price(150000).build(),
                GradePriceInfo.builder().grade("S").price(100000).build(),
                GradePriceInfo.builder().grade("R").price(50000).build()
        );
        //공연 등록
        RegisterConcertRequest concertDto = ConcertCreateBuilder.builder().gradePrices(gradePriceInfos).venueId(venue.getId()).build();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "이미지데이터".getBytes()
        );
        RegisterConcertResponse response = registerConcertService.register(concertDto,multipartFile);

        //공연 일정 등록
        List<SessionInfo> sessionInfos = List.of(
                SessionInfo.builder()
                        .concertDate(LocalDate.of(2025, 6, 1))
                        .startTime(LocalTime.of(15, 0)).build()
        );

        RegisterSessionRequest sessionDto = RegisterSessionRequest.builder()
                .concertId(response.getConcertId())
                .sessionInfos(sessionInfos)
                .build();
        sessionIds = registerSessionService.register(sessionDto);

        idByConcertId = seatRepository.findIdByConcertId(response.getConcertId());
    }

    @Test
    @DisplayName("예약에 성공한다.")
    void 예약_성공() {
//        given
        BookingRequest requestDto = BookingRequest.builder()
                .sessionId(sessionIds.get(0))
                .seatIds(List.of(idByConcertId.get(0), idByConcertId.get(1)))
                .build();
        Member test = memberRepository.save(Member.create("test", "test@test", "@Abc", Role.ADMIN));

        //when
        BookingRef bookingRef = bookingService.booking(requestDto,test.getEmail());

        //then
        Booking booking = bookingRepository.findById(bookingRef).get();
        Assertions.assertThat(booking.getTotalAmounts().getValue()).isEqualTo(150000 + 150000);
    }

}
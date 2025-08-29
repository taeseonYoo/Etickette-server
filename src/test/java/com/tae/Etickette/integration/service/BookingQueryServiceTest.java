package com.tae.Etickette.integration.service;

import com.tae.Etickette.booking.command.application.BookingService;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.query.application.PaymentInfo;
import com.tae.Etickette.booking.query.application.BookingQueryService;
import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.command.domain.ImageUploader;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.seat.infra.SeatRepository;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.SessionCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@SpringBootTest
@Transactional
class BookingQueryServiceTest {
    @Autowired
    BookingQueryService bookingQueryService;

    @Autowired
    RegisterVenueService registerVenueService;
    @Autowired
    RegisterConcertService registerConcertService;
    @Autowired
    RegisterSessionService registerSessionService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BookingService bookingService;
    @Autowired
    SeatRepository seatRepository;
    @MockitoBean
    ImageUploader imageUploader;

    @Test
    @DisplayName("Booking후에 결제 해야하는 정보를 가져온다.")
    void 결제정보가져오기_성공() {

        Member savedMember = memberRepository.save(Member.create("user", "user@spring", "@Abc12", Role.ADMIN));
        RegisterVenueRequest venueRequest = VenueCreateBuilder.builder().build();
        RegisterVenueResponse venueResponse = registerVenueService.register(venueRequest);

        RegisterConcertRequest concertRequest = ConcertCreateBuilder.builder().venueId(venueResponse.getId()).build();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "이미지데이터".getBytes()
        );
        BDDMockito.given(imageUploader.upload(multipartFile)).willReturn("amazonaws.com");
        RegisterConcertResponse response = registerConcertService.register(concertRequest,multipartFile);

        RegisterSessionRequest sessionRequest = SessionCreateBuilder.builder().concertId(response.getConcertId()).sessionInfos(List.of(RegisterSessionRequest.SessionInfo.builder().concertDate(LocalDate.of(2025, 6, 6)).startTime(LocalTime.of(15, 0)).build())).build();
        List<Long> sessions = registerSessionService.register(sessionRequest);

        List<Long> seatIds = seatRepository.findIdByConcertId(response.getConcertId());
        BookingRequest request = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(0))).build();
        BookingRef bookingRef = bookingService.booking(request, savedMember.getEmail());

        PaymentInfo paymentInfo = bookingQueryService.getPaymentInfo(bookingRef,savedMember.getEmail());

        Assertions.assertThat(paymentInfo.getBookingRef()).isEqualTo(bookingRef.getValue());
        Assertions.assertThat(paymentInfo.getTotalAmounts()).isEqualTo(150000);
        Assertions.assertThat(paymentInfo.getDetails().get(0).getSeatInfo()).isEqualTo("A1");
    }
}
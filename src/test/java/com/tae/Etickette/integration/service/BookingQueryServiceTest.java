package com.tae.Etickette.integration.service;

import com.tae.Etickette.booking.command.application.BookingService;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.command.domain.BookingStatus;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.booking.query.BookingSummary;
import com.tae.Etickette.booking.query.application.PaymentInfo;
import com.tae.Etickette.booking.query.application.BookingQueryService;
import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.command.domain.ImageUploader;
import com.tae.Etickette.global.exception.BadRequestException;
import com.tae.Etickette.global.exception.ErrorCode;
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
import org.junit.jupiter.api.BeforeEach;
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
    BookingRepository bookingRepository;
    @Autowired
    BookingService bookingService;
    @Autowired
    SeatRepository seatRepository;
    @MockitoBean
    ImageUploader imageUploader;

    Member member;
    List<Long> sessions;
    List<Long> seatIds;
    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.create("user", "user@spring", "@Abc12", Role.ADMIN));
        RegisterVenueRequest venueRequest = VenueCreateBuilder.builder().build();
        RegisterVenueResponse venueResponse = registerVenueService.register(venueRequest);

        RegisterConcertRequest concertRequest = ConcertCreateBuilder.builder().venueId(venueResponse.getId()).build();
        MockMultipartFile multipartFile = new MockMultipartFile("image", "testImage.png", "image/png", "이미지데이터".getBytes());
        BDDMockito.given(imageUploader.upload(multipartFile)).willReturn("amazonaws.com");
        RegisterConcertResponse response = registerConcertService.register(concertRequest,multipartFile);

        RegisterSessionRequest sessionRequest = SessionCreateBuilder.builder().concertId(response.getConcertId()).sessionInfos(List.of(RegisterSessionRequest.SessionInfo.builder().concertDate(LocalDate.of(2025, 6, 6)).startTime(LocalTime.of(15, 0)).build())).build();
        sessions = registerSessionService.register(sessionRequest);
        seatIds = seatRepository.findIdByConcertId(response.getConcertId());
    }

    @Test
    @DisplayName("좌석을 예약하고, 결제를 위해 정보를 가져온다.")
    void getPaymentInfo_1() {
        //given
        BookingRequest request = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(0))).build();
        BookingRef bookingRef = bookingService.booking(request, member.getEmail());

        PaymentInfo paymentInfo = bookingQueryService.getPaymentInfo(bookingRef,member.getEmail());

        Assertions.assertThat(paymentInfo.getBookingRef()).isEqualTo(bookingRef.getValue());
        Assertions.assertThat(paymentInfo.getTotalAmounts()).isEqualTo(150000);
        Assertions.assertThat(paymentInfo.getDetails().get(0).getSeatInfo()).isEqualTo("A1");
    }

    @Test
    @DisplayName("예매요약내역 조회에 성공하면 값이 일치해야한다.")
    void getBookingList_1() {
        //given
        BookingRequest request = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(0),seatIds.get(1))).build();
        BookingRef bookingRef = bookingService.booking(request, member.getEmail());

        //when
        List<BookingSummary> bookingList = bookingQueryService.getBookingList(member.getEmail(),null);

        //then
        BookingSummary bookingSummary = bookingList.get(0);
        Assertions.assertThat(bookingSummary.getBookingRef()).isEqualTo(bookingRef.getValue());
        Assertions.assertThat(bookingSummary.getConcertName()).isEqualTo("IU HER");
        Assertions.assertThat(bookingSummary.getConcertDate()).isEqualTo(LocalDate.of(2025,6,6));
        Assertions.assertThat(bookingSummary.getConcertTime()).isEqualTo(LocalTime.of(15, 0));
        Assertions.assertThat(bookingSummary.getStatus()).isEqualTo(BookingStatus.BEFORE_PAY);
        Assertions.assertThat(bookingSummary.getMemberId()).isEqualTo(member.getId());
        Assertions.assertThat(bookingSummary.getVenueName()).isEqualTo("KSPO");
        Assertions.assertThat(bookingSummary.getImagePath()).isEqualTo("amazonaws.com");
        Assertions.assertThat(bookingList.get(0).getSize()).isEqualTo(2);
    }
    @Test
    @DisplayName("BEFORE_PAY 조회에 성공하면, BEFORE_PAY 인 값만 조회되어야한다.")
    void getBookingList_BEFORE_PAY() {
        //given
        BookingRequest request1 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(0),seatIds.get(1))).build();
        BookingRef bookingRef1 = bookingService.booking(request1, member.getEmail());
        bookingRepository.findById(bookingRef1).get().cancel();

        BookingRequest request2 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(2),seatIds.get(3))).build();
        BookingRef bookingRef2 = bookingService.booking(request2, member.getEmail());
        bookingRepository.findById(bookingRef2).get().confirmPayment(1L);

        BookingRequest request3 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(4),seatIds.get(5))).build();
        BookingRef bookingRef3 = bookingService.booking(request3, member.getEmail());

        //when
        List<BookingSummary> bookingListByStatus = bookingQueryService.getBookingList(member.getEmail(), "BEFORE_PAY");

        //then
        Assertions.assertThat(bookingListByStatus.size()).isEqualTo(1);
        Assertions.assertThat(bookingListByStatus.get(0).getBookingRef()).isEqualTo(bookingRef3.getValue());
    }

    @Test
    @DisplayName("COMPLETED_BOOKING 조회에 성공하면, COMPLETED_BOOKING 인 값만 조회되어야한다.")
    void getBookingList_COMPLETED_BOOKING() {
        //given
        BookingRequest request1 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(0),seatIds.get(1))).build();
        BookingRef bookingRef1 = bookingService.booking(request1, member.getEmail());
        bookingRepository.findById(bookingRef1).get().cancel();

        BookingRequest request2 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(2),seatIds.get(3))).build();
        BookingRef bookingRef2 = bookingService.booking(request2, member.getEmail());
        bookingRepository.findById(bookingRef2).get().confirmPayment(1L);

        BookingRequest request3 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(4),seatIds.get(5))).build();
        BookingRef bookingRef3 = bookingService.booking(request3, member.getEmail());

        //when
        List<BookingSummary> bookingListByStatus = bookingQueryService.getBookingList(member.getEmail(), "COMPLETED_BOOKING");

        //then
        Assertions.assertThat(bookingListByStatus.size()).isEqualTo(1);
        Assertions.assertThat(bookingListByStatus.get(0).getBookingRef()).isEqualTo(bookingRef2.getValue());
    }
    @Test
    @DisplayName("CANCELED_BOOKING 조회에 성공하면, CANCELED_BOOKING 인 값만 조회되어야한다.")
    void getBookingList_CANCELED_BOOKING() {
        //given
        BookingRequest request1 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(0),seatIds.get(1))).build();
        BookingRef bookingRef1 = bookingService.booking(request1, member.getEmail());
        bookingRepository.findById(bookingRef1).get().cancel();

        BookingRequest request2 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(2),seatIds.get(3))).build();
        BookingRef bookingRef2 = bookingService.booking(request2, member.getEmail());
        bookingRepository.findById(bookingRef2).get().confirmPayment(1L);

        BookingRequest request3 = BookingRequest.builder().sessionId(sessions.get(0)).seatIds(List.of(seatIds.get(4),seatIds.get(5))).build();
        BookingRef bookingRef3 = bookingService.booking(request3, member.getEmail());

        //when
        List<BookingSummary> bookingListByStatus = bookingQueryService.getBookingList(member.getEmail(), "CANCELED_BOOKING");

        //then
        Assertions.assertThat(bookingListByStatus.size()).isEqualTo(1);
        Assertions.assertThat(bookingListByStatus.get(0).getBookingRef()).isEqualTo(bookingRef1.getValue());
    }

    @Test
    @DisplayName("잘못된 상태값이 입력되면, 예외가 발생한다.")
    void getBookingList_invalid() {
        //when
        Assertions.assertThatThrownBy(() ->
                        bookingQueryService.getBookingList(member.getEmail(), "fake_status"))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKING_QUERY_INVALID_STATUS);
    }

}
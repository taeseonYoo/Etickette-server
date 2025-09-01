package com.tae.Etickette.booking.query.application;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.command.domain.BookingStatus;
import com.tae.Etickette.booking.command.domain.SeatItem;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.booking.query.BookingSummary;
import com.tae.Etickette.booking.query.BookingSummaryDao;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.global.exception.UnauthorizedException;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.seat.query.SeatData;
import com.tae.Etickette.seat.query.SeatQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - BookingQueryService 단위 테스트")
class BookingQueryServiceTest {
    @InjectMocks
    BookingQueryService bookingQueryService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    SeatQueryService seatQueryService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    BookingSummaryDao bookingSummaryDao;


    @Test
    @DisplayName("결제정보조회 - 결제정보조회에 성공한다.")
    void 결제정보조회_성공() {
        //given
        Booking booking = Booking.create(1L, 1L, List.of(new SeatItem(new BookSeatId(1L, 1L), new Money(10000))));
        given(bookingRepository.findById(any())).willReturn(Optional.of(booking));
        Member member = Member.create("test", "test@spring", "1234", Role.USER);
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberRepository.findByEmail("test@spring")).willReturn(Optional.of(member));
        given(seatQueryService.getSeat(anyLong())).willReturn(Optional.of(mock(SeatData.class)));

        //when
        PaymentInfo paymentInfo = bookingQueryService.getPaymentInfo(booking.getBookingRef(), "test@spring");

        //then
        Assertions.assertThat(paymentInfo.getBookingRef()).isEqualTo(booking.getBookingRef().getValue());
        Assertions.assertThat(paymentInfo.getBookerName()).isEqualTo("test");
        Assertions.assertThat(paymentInfo.getBookerEmail()).isEqualTo("test@spring");
        Assertions.assertThat(paymentInfo.getTotalAmounts()).isEqualTo(10000);
    }
    @Test
    @DisplayName("결제정보조회 - 예약내역이 없다면 예외를 던진다.")
    void 결제정보조회_실패_예약내역없음() {
        //given
        BookingRef bookingRef = new BookingRef("ref1234");
        given(bookingRepository.findById(bookingRef)).willReturn(Optional.empty());

        //when
        Assertions.assertThatThrownBy(() ->
                bookingQueryService.getPaymentInfo(bookingRef, "test@spring"))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKING_NOT_FOUND);
    }
    @Test
    @DisplayName("결제정보조회 - 회원이 존재하지 않다면 예외를 던진다.")
    void 결제정보조회_실패_회원정보없음() {
        //given
        BookingRef bookingRef = new BookingRef("ref1234");
        given(bookingRepository.findById(bookingRef)).willReturn(Optional.of(mock(Booking.class)));
        given(memberRepository.findByEmail("test@spring")).willReturn(Optional.empty());

        //when
        Assertions.assertThatThrownBy(() ->
                        bookingQueryService.getPaymentInfo(bookingRef, "test@spring"))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }
    @Test
    @DisplayName("결제정보조회 - 예약자가 아니라면 예외를 던진다.")
    void 결제정보조회_실패_예약자불일치() {
        //given
        BookingRef bookingRef = new BookingRef("ref1234");
        given(bookingRepository.findById(bookingRef)).willReturn(Optional.of(mock(Booking.class)));
        Member member = Member.create("test", "test@spring", "1234", Role.USER);
        ReflectionTestUtils.setField(member, "id", 2L);
        given(memberRepository.findByEmail("test@spring")).willReturn(Optional.of(member));

        //when
        Assertions.assertThatThrownBy(() ->
                        bookingQueryService.getPaymentInfo(bookingRef, "test@spring"))
                .isInstanceOf(UnauthorizedException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.UNAUTHORIZED);
    }
    @Test
    @DisplayName("결제정보조회 - 좌석이 존재하지 않다면 예외를 던진다.")
    void 결제정보조회_실패_좌석정보없음() {
        //given
        Booking booking = Booking.create(1L, 1L, List.of(new SeatItem(new BookSeatId(1L, 1L), new Money(10000))));
        BookingRef bookingRef = new BookingRef("ref1234");
        given(bookingRepository.findById(bookingRef)).willReturn(Optional.of(booking));
        Member member = Member.create("test", "test@spring", "1234", Role.USER);
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberRepository.findByEmail("test@spring")).willReturn(Optional.of(member));
        given(seatQueryService.getSeat(anyLong())).willReturn(Optional.empty());

        //when
        Assertions.assertThatThrownBy(() ->
                        bookingQueryService.getPaymentInfo(bookingRef, "test@spring"))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SEAT_NOT_FOUND);
    }

    @Test
    @DisplayName("예매목록조회 - 회원이 존재하면 예매내역을 반환한다.")
    void 예매목록조회_성공() {
        //given
        Member member = Member.create("test", "test@spring", "@Abc", Role.USER);
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberRepository.findByEmail("test@spring")).willReturn(Optional.of(member));

        BookingSummary summary = mock(BookingSummary.class);
        given(summary.getBookingRef()).willReturn("ref1234");
        given(summary.getConcertName()).willReturn("IU HER");
        List<BookingSummary> summaries = List.of(summary);
        given(bookingSummaryDao.findBookingSummariesByMemberId(anyLong())).willReturn(summaries);

        //when
        List<BookingSummary> bookingList = bookingQueryService.getBookingList("test@spring");

        //that
        assertThat(bookingList.get(0).getBookingRef()).isEqualTo("ref1234");
        assertThat(bookingList.get(0).getConcertName()).isEqualTo("IU HER");
    }
    @Test
    @DisplayName("예매목록조회 - 회원이 존재하지 않으면 예외를 던진다.")
    void 예매목록조회_실패_회원정보없음() {
        given(memberRepository.findByEmail("fakeuser@spring")).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> bookingQueryService.getBookingList("fakeuser@spring")
        );
    }

}
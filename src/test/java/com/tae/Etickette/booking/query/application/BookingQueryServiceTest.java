package com.tae.Etickette.booking.query.application;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.command.domain.BookingStatus;
import com.tae.Etickette.booking.command.domain.SeatItem;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.booking.query.BookingSummary;
import com.tae.Etickette.booking.query.BookingSummaryDao;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.global.exception.BadRequestException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    Member member;
    @BeforeEach
    void setUp() {
        member = Member.create("test", "test@spring", "1234", Role.USER);
        ReflectionTestUtils.setField(member, "id", 1L);
    }
    @Test
    @DisplayName("결제정보조회에 성공하면, 값이 일치해야한다.")
    void getPaymentInfo_1() {
        //given
        Booking booking = Booking.create(1L, 1L, List.of(new SeatItem(new BookSeatId(1L, 1L), new Money(10000))));
        given(bookingRepository.findById(any())).willReturn(Optional.of(booking));
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
    @DisplayName("예약내역이 없다면 예외를 던진다.")
    void getPaymentInfo_2() {
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
    @DisplayName("회원이 존재하지 않다면 예외를 던진다.")
    void getPaymentInfo_3() {
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
    @DisplayName("예약자가 아니라면 예외를 던진다.")
    void getPaymentInfo_4() {
        //given
        BookingRef bookingRef = new BookingRef("ref1234");
        given(bookingRepository.findById(bookingRef)).willReturn(Optional.of(mock(Booking.class)));
        given(memberRepository.findByEmail("test@spring")).willReturn(Optional.of(member));

        //when
        Assertions.assertThatThrownBy(() ->
                        bookingQueryService.getPaymentInfo(bookingRef, "test@spring"))
                .isInstanceOf(UnauthorizedException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.UNAUTHORIZED);
    }
    @Test
    @DisplayName("좌석이 존재하지 않다면 예외를 던진다.")
    void getPaymentInfo_5() {
        //given
        Booking booking = Booking.create(1L, 1L, List.of(new SeatItem(new BookSeatId(1L, 1L), new Money(10000))));
        BookingRef bookingRef = new BookingRef("ref1234");
        given(bookingRepository.findById(bookingRef)).willReturn(Optional.of(booking));
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
    @DisplayName("예매내역조회에 성공하면, 값이 일치해야한다.")
    void getBookingList_1() {
        //given
        given(memberRepository.findByEmail("test@spring")).willReturn(Optional.of(member));

        BookingSummary summary = mock(BookingSummary.class);
        given(summary.getBookingRef()).willReturn("ref1234");
        given(summary.getConcertName()).willReturn("IU HER");
        List<BookingSummary> summaries = List.of(summary);
        given(bookingSummaryDao.findBookingSummariesByMemberId(anyLong())).willReturn(summaries);

        //when
        List<BookingSummary> bookingList = bookingQueryService.getBookingList("test@spring",null);

        //that
        assertThat(bookingList.get(0).getBookingRef()).isEqualTo("ref1234");
        assertThat(bookingList.get(0).getConcertName()).isEqualTo("IU HER");
    }
    @Test
    @DisplayName("예매내역조회 시, 회원이 존재하지 않으면 예외를 던진다.")
    void getBookingList_2() {
        given(memberRepository.findByEmail("fakeuser@spring")).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> bookingQueryService.getBookingList("fakeuser@spring", null)
        );
    }
    @Test
    @DisplayName("예매내역조회 시,회원이 존재하지 않으면 예외를 던진다.")
    void getBookingList_4() {
        //given
        BDDMockito.given(memberRepository.findByEmail("test@spring")).willReturn(Optional.empty());

        //when
        assertThatThrownBy(
                () -> bookingQueryService.getBookingList("fakeuser@spring","BEFORE_PAY")
        );
    }
    @Test
    @DisplayName("예매내역조회 시,상태가 null 이면 findBookingSummariesByMemberId 를 호출한다.")
    void getBookingList_5() {
        //given
        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        bookingQueryService.getBookingList("test@spring", null);

        verify(bookingSummaryDao, times(1))
                .findBookingSummariesByMemberId(any());
    }
    @Test
    @DisplayName("예매내역조회 시, 상태가 BEFORE_PAY 이면 findBookingSummariesByMemberIdAndStatus 를 호출한다.")
    void getBookingList_6() {
        //given
        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        bookingQueryService.getBookingList("test@spring", "BEFORE_PAY");

        verify(bookingSummaryDao, times(1))
                .findBookingSummariesByMemberIdAndStatus(any(), any());
    }
    @Test
    @DisplayName("예매내역조회 시, 상태가 COMPLETED_BOOKING 이면 findBookingSummariesByMemberIdAndStatus 를 호출한다.")
    void getBookingList_7() {
        //given
        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        bookingQueryService.getBookingList("test@spring", "COMPLETED_BOOKING");

        verify(bookingSummaryDao, times(1))
                .findBookingSummariesByMemberIdAndStatus(any(), any());
    }
    @Test
    @DisplayName("예매내역조회 시,상태가 CANCELED_BOOKING 이면 findBookingSummariesByMemberIdAndStatus 를 호출한다.")
    void getBookingList_8() {
        //given
        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        bookingQueryService.getBookingList("test@spring", "CANCELED_BOOKING");

        verify(bookingSummaryDao, times(1))
                .findBookingSummariesByMemberIdAndStatus(any(), any());
    }
    @Test
    @DisplayName("예매내역조회 시, 상태가 INVALID 하면, 예외를 던진다.")
    void getBookingList_9() {
        //given
        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        Assertions.assertThatThrownBy(() -> bookingQueryService.getBookingList("test@spring", "INVALID"))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKING_QUERY_INVALID_STATUS);
    }

}
package com.tae.Etickette.booking.query.application;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.global.exception.UnauthorizedException;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.seat.query.SeatData;
import com.tae.Etickette.seat.query.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingQueryService {
    private final BookingRepository bookingRepository;
    private final SeatQueryService seatQueryService;

    private final MemberRepository memberRepository;

    public PaymentInfo getPaymentInfo(BookingRef bookingRef,String email) {
        Booking booking = bookingRepository.findById(bookingRef)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND, "예약 정보를 찾을 수 없습니다."));

        Member member = memberRepository
                .findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        if (!member.getId().equals(booking.getMemberId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED, "예약 정보와 회원 정보가 일치하지 않습니다.");
        }

        List<LineItemInfo> lineItems = booking.getSeatItems().stream()
                .map(lineItem -> {
                    SeatData seatData = seatQueryService.getSeat(lineItem.getSeatId().getSeatId())
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SEAT_NOT_FOUND, "좌석을 찾을 수 없습니다."));
                    return new LineItemInfo(lineItem, seatData);
                }).toList();

        return new PaymentInfo(booking, lineItems, new Booker(member.getName(), email));
    }
}

package com.tae.Etickette.booking.query;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("잘못된 예약 번호입니다."));

        Member member = memberRepository
                .findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원 정보가 없음."));

        if (!member.getId().equals(booking.getMemberId())) {
            throw new RuntimeException("예외");
        }

        List<LineItemInfo> lineItems = booking.getSeatItems().stream()
                .map(lineItem -> {
                    SeatData seatData = seatQueryService.getSeat(lineItem.getSeatId().getSeatId())
                            .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다."));
                    return new LineItemInfo(lineItem, seatData);
                }).toList();

        return new PaymentInfo(booking, lineItems, new Booker(member.getName(), email));
    }
}

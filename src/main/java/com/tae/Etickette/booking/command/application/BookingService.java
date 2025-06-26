package com.tae.Etickette.booking.command.application;

import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.command.domain.SeatItem;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.member.application.MemberNotFoundException;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;
    private final BookSeatRepository bookSeatRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public BookingRef booking(BookingRequest requestDto, String email) {

        Session session = sessionRepository.findById(requestDto.getSessionId()).orElseThrow(() ->
                new SessionNotFoundException("세션을 찾을 수 없습니다."));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        //TODO 회원이 이미 예약한 좌석의 갯수가 몇개인지 확인해야 한다. -> 고민 좀 해보자

        //주문 목록 생성
        List<SeatItem> seatItems = new ArrayList<>();
        for (Long seatId : requestDto.getSeatIds()) {
            BookSeat seat = bookSeatRepository.findById(new BookSeatId(seatId, requestDto.getSessionId()))
                    .orElseThrow(() -> new BookSeatNotFoundException("스케줄 좌석 정보가 없습니다."));

            //좌석을 잠금 상태로 설정
            seat.lock();
            seatItems.add(new SeatItem(new BookSeatId(seatId, session.getId()), seat.getPrice()));
        }

        Booking booking = Booking.create(member.getId(), requestDto.getSessionId(), seatItems);
        Booking savedBooking = bookingRepository.save(booking);

        return savedBooking.getBookingRef();
    }
}

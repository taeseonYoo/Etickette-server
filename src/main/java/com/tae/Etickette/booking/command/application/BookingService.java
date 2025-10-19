package com.tae.Etickette.booking.command.application;

import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.command.domain.SeatItem;
import com.tae.Etickette.booking.command.domain.SeatLockedEvent;
import com.tae.Etickette.booking.command.domain.SeatScheduler;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;
    private final BookSeatRepository bookSeatRepository;
    private final MemberRepository memberRepository;
    private final SeatScheduler scheduler;
    @Transactional
    public BookingRef booking(BookingRequest requestDto, String email) {

        Session session = sessionRepository.findById(requestDto.getSessionId()).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.SESSION_NOT_FOUND, "세션을 찾을 수 없습니다. 세션 번호:" + requestDto.getSessionId()));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,"회원 정보를 찾을 수 없습니다."));

        //데드락 방지를 위해 좌석의 ID를 오름차순 정렬한다.
        requestDto.getSeatIds().sort(Comparator.naturalOrder());

        //주문 목록 생성
        List<SeatItem> seatItems = new ArrayList<>();
        for (Long seatId : requestDto.getSeatIds()) {
            BookSeat seat = bookSeatRepository.findByIdWithLock(seatId, requestDto.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKSEAT_NOT_FOUND, "좌석 정보를 찾을 수 없습니다."));
            //좌석을 잠금 상태로 설정
            seat.lock();
            seatItems.add(new SeatItem(new BookSeatId(seatId, session.getId()), seat.getPrice()));
        }
        Booking booking = Booking.create(member.getId(), requestDto.getSessionId(), seatItems);

        //좌석 선점 해제 스케줄링 이벤트 발행
        Events.raise(new SeatLockedEvent(requestDto.getSeatIds(), session.getId()));

        return bookingRepository.save(booking).getBookingRef();
    }
}

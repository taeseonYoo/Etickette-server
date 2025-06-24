package com.tae.Etickette.booking.command.application;

import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.command.domain.LineItem;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.bookseat.domain.BookSeat;
import com.tae.Etickette.bookseat.domain.BookSeatId;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
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
    @Transactional
    public BookingRef booking(BookingRequest requestDto) {

        Session session = sessionRepository.findById(requestDto.getSessionId()).orElseThrow(() ->
                new SessionNotFoundException("세션을 찾을 수 없습니다."));

        //주문 목록 생성
        List<LineItem> lineItems = new ArrayList<>();
        for (Long seatId : requestDto.getSeatIds()) {
            BookSeat seat = bookSeatRepository.findById(new BookSeatId(seatId, requestDto.getSessionId()))
                    .orElseThrow(() -> new BookSeatNotFoundException("스케줄 좌석 정보가 없습니다."));

            //좌석을 잠금 상태로 설정
            seat.lock();
            lineItems.add(new LineItem(new BookSeatId(seatId, session.getId()), seat.getPrice()));
        }

        Booking booking = Booking.create(requestDto.getMemberId(), requestDto.getSessionId(),lineItems);
        Booking savedBooking = bookingRepository.save(booking);

        return savedBooking.getBookingRef();
    }
}

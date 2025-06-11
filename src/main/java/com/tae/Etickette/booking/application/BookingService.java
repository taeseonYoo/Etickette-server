package com.tae.Etickette.booking.application;

import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.application.Dto.BookingRequestDto;
import com.tae.Etickette.global.model.Seat;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tae.Etickette.booking.domain.BookingServiceHelper.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;
    @Transactional
    public BookingRef booking(BookingRequestDto requestDto) {

        Session session = sessionRepository.findById(requestDto.getSessionId()).orElseThrow(() ->
                new SessionNotFoundException("세션을 찾을 수 없습니다."));

        List<Seat> requestSeats = requestDto.getSeatInfos().stream()
                .map(seatInfo -> new Seat(seatInfo.getRow(), seatInfo.getColumn()))
                .toList();

        List<Seat> bookedSeats = bookingRepository.findBySessionId(requestDto.getSessionId())
                .stream().flatMap(b -> b.getSeats().stream()).toList();


        //좌석이 예매 가능한 지 검증
        verifySeatsNotAlreadyBooked(session, bookedSeats, requestSeats);
        //좌석 정보를 입력한다.
        List<Seat> seats = registerSeatInfo(requestSeats, session.getSeatingPlan());


        Booking booking = Booking.create(requestDto.getMemberId(), requestDto.getSessionId(), seats);
        Booking savedBooking = bookingRepository.save(booking);

        return savedBooking.getBookingRef();
    }
}

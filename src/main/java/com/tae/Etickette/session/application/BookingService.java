package com.tae.Etickette.session.application;

import com.tae.Etickette.Booking.BookingRepository;
import com.tae.Etickette.Booking.domain.Booking;
import com.tae.Etickette.Booking.domain.BookingPolicy;
import com.tae.Etickette.session.domain.Seat;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;
    private final BookingPolicy bookingPolicy;

    @Transactional
    public Long booking(BookingRequestDto requestDto) {

        Session session = sessionRepository.findById(requestDto.getSessionId()).orElseThrow(() -> new RuntimeException());

        List<Seat> requestSeats = requestDto.getSeatInfos().stream()
                .map(seatInfo -> new Seat(seatInfo.getRow(), seatInfo.getColumn()))
                .toList();

        List<Seat> bookedSeats = bookingRepository.findBySessionId(requestDto.getSessionId())
                .stream().flatMap(b -> b.getSeats().stream()).toList();

        
        //좌석이 예매 가능한 지 검증
        bookingPolicy.verifySeatsNotAlreadyBooked(session, bookedSeats, requestSeats);


        Booking booking = Booking.create(requestDto.getMemberId(), requestDto.getSessionId(), requestSeats);
        bookingRepository.save(booking);


        return session.getId();
    }
}

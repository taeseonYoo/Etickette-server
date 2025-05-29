package com.tae.Etickette.booking;

import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.GradePrice;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.schedule.domain.Schedule;
import com.tae.Etickette.schedule.infra.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.tae.Etickette.booking.BookingRequestDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;
    private final ConcertRepository concertRepository;

    @Transactional
    public void booking(BookingRequestDto requestDto) {
        //좌석 예매 가능 여부 검증
        List<Booking> bookingList = bookingRepository.findByScheduleId(requestDto.getScheduleId());

        List<Seat> bookedSeatList = bookingList.stream()
                .flatMap(b -> b.getBookedSeats().stream())
                .map(BookedSeat::getSeat)
                .toList();


        List<Seat> seatList = requestDto.getSeats()
                .stream().map(SeatInfo::toSeat).toList();

        //예약을 원하는 좌석을 검증한다.
        for (Seat seat : seatList) {
            if (bookedSeatList.contains(seat)) {
                throw new RuntimeException();
            }
        }

        //죄석 가격 계산
        Schedule schedule = scheduleRepository
                .findById(requestDto.getScheduleId()).orElseThrow(()->new ScheduleNotFoundException("스케줄이 없습니다."));
        Concert concert = concertRepository
                .findById(schedule.getConcertId()).orElseThrow(()->new ConcertNotFoundException("공연이 없습니다."));

        List<GradePrice> gradePrices = concert.getGradePrices();


        List<BookedSeat> newBookedSeat = new ArrayList<>();
        for (SeatInfo seatInfo : requestDto.getSeats()) {
            for (GradePrice gradePrice : gradePrices) {

                if (gradePrice.getGrade().equals(seatInfo.getGrade())) {
                    Seat seat = seatInfo.toSeat();
                    newBookedSeat.add(new BookedSeat(seat, gradePrice.getPrice()));
                }
            }
        }

        //TODO 결제를 진행한다.

        Booking booking = new Booking(newBookedSeat, requestDto.getScheduleId(), requestDto.getMemberId());
        bookingRepository.save(booking);
    }
}

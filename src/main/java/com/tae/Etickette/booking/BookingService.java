package com.tae.Etickette.booking;

import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.GradePrice;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.schedule.domain.Schedule;
import com.tae.Etickette.schedule.infra.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

        //죄석 가격 계산
        Schedule schedule = scheduleRepository
                .findById(requestDto.getScheduleId()).orElseThrow(()->new ScheduleNotFoundException("스케줄이 없습니다."));
        Concert concert = concertRepository
                .findById(schedule.getConcertId()).orElseThrow(()->new ConcertNotFoundException("공연이 없습니다."));

        List<GradePrice> gradePrices = concert.getGradePrices();



        //TODO 결제를 진행한다.

        Booking booking = new Booking( requestDto.getScheduleId(), requestDto.getMemberId());
        bookingRepository.save(booking);
    }
}

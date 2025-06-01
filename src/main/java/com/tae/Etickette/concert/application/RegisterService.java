package com.tae.Etickette.concert.application;

import com.tae.Etickette.TicketRepository;
import com.tae.Etickette.concert.domain.*;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import com.tae.Etickette.schedule.domain.Schedule;
import com.tae.Etickette.schedule.domain.Ticket;
import com.tae.Etickette.schedule.infra.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterService {
    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final ScheduleRepository scheduleRepository;
    private final TicketRepository ticketRepository;


    @Transactional
    public Long createConcert(ConcertCreateRequestDto requestDto) {
        Venue venue = venueRepository.findById(requestDto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다"));
        List<Seat> seats = venue.getSeats();

        List<GradePrice> gradePrices = requestDto.toSectionEntities();

        //콘서트 생성
        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgUrl(),
                requestDto.getVenueId(),
                gradePrices);

        List<Schedule> schedules = requestDto.toScheduleEntities();

        //공연장 별 스케줄 일정 검증
        for (Schedule schedule : schedules) {
            List<Schedule> findSchedules = scheduleRepository
                    .findByConcertDateAndVenueId(schedule.getConcertDate(), venue.getId())
                    .stream()
                    .filter(Schedule::isActive)
                    .toList();

            if (!findSchedules.isEmpty()) {
                throw new ScheduleDuplicateException();
            }
            scheduleRepository.save(schedule);


            for (Seat seat : seats) {
                ticketRepository.save(new Ticket(schedule.getId(), seat.getId()));
            }

        }

        //콘서트 저장
        Concert savedConcert = concertRepository.save(concert);

        return savedConcert.getId();
    }

}

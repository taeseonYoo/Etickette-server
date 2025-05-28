package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.domain.*;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import com.tae.Etickette.schedule.domain.Schedule;
import com.tae.Etickette.schedule.infra.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final ScheduleRepository scheduleRepository;


    @Transactional
    public Long createConcert(ConcertCreateRequestDto requestDto) {
        Venue venue = venueRepository.findById(requestDto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다"));

        List<Grade> grades = requestDto.toSectionEntities();

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgUrl(),
                venue,
                grades);

        Concert savedConcert = concertRepository.save(concert);


        List<ConcertCreateRequestDto.ScheduleInfo> scheduleInfos = requestDto.getScheduleInfos();
        for (ConcertCreateRequestDto.ScheduleInfo scheduleInfo : scheduleInfos) {
            Schedule schedule = Schedule.create(scheduleInfo.getDate(), scheduleInfo.getTime(), concert.getRunningTime(), concert);
            List<Schedule> conflictingSchedules = scheduleRepository.findConflictingSchedules(venue, schedule.getConcertDate(), schedule.getStartTime(), schedule.getEndTime());
            if (!conflictingSchedules.isEmpty()) {
                throw new RuntimeException();
            }
            scheduleRepository.save(schedule);
        }

        return savedConcert.getId();
    }

}

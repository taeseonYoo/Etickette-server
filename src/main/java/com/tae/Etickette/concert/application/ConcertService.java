package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.domain.*;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public Long createConcert(ConcertCreateRequestDto requestDto) {
        Venue venue = venueRepository.findById(requestDto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다"));

        List<Schedule> schedules = requestDto.toScheduleEntities();
        List<Grade> grades = requestDto.toSectionEntities();

        //TODO 스케쥴을 검증해야한다.
        List<Concert> byVenueId = concertRepository.findByVenueIdAndStatusNot(venue.getId(), ConcertStatus.FINISHED);

        List<Schedule> allSchedules = byVenueId.stream()
                .flatMap(concert -> concert.getSchedules().stream())
                .toList();

        for (Schedule schedule : schedules) {
            if (allSchedules.contains(schedule)) {
                throw new RuntimeException();
            }
        }

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgUrl(),
                venue,
                grades,
                schedules);

        Concert savedConcert = concertRepository.save(concert);
        return savedConcert.getId();
    }

}

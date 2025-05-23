package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.Schedule;
import com.tae.Etickette.concert.domain.Section;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public void createConcert(ConcertCreateRequestDto requestDto) {
        Venue venue = venueRepository.findById(requestDto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다"));

        LocalDate startAt = requestDto.getScheduleInfos().stream()
                .map(ConcertCreateRequestDto.ScheduleInfo::getDate)
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("스케줄 정보가 없습니다."));
        LocalDate endAt = requestDto.getScheduleInfos().stream()
                .map(ConcertCreateRequestDto.ScheduleInfo::getDate)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("스케줄 정보가 없습니다."));

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                startAt,
                endAt,
                requestDto.getRunningTime());

        //공연장 연관관계 설정
        concert.addVenue(venue);

        //섹션 연관관계 설정
        requestDto.getSectionInfos().forEach(info -> {
            Section section = Section.create(info.getGrade(), info.getPrice());
            concert.addSection(section);
        });

        //스케쥴 연관관계 설정
        requestDto.getScheduleInfos().forEach(info -> {
            Schedule schedule = Schedule.create(info.getDate(), info.getTime());
            concert.addSchedule(schedule);
        });

        concertRepository.save(concert);
    }

}

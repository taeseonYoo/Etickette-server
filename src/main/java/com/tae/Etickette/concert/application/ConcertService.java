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
        List<Section> sections = requestDto.toSectionEntities();

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgUrl(),
                venue,
                sections,
                schedules);

        Concert savedConcert = concertRepository.save(concert);
        return savedConcert.getId();
    }

}

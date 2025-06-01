package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.domain.*;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.application.VenueNotFoundException;
import com.tae.Etickette.venue.domain.Venue;
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
    private final SessionRepository sessionRepository;


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

        List<Session> sessions = requestDto.toScheduleEntities();





        //콘서트 저장
        Concert savedConcert = concertRepository.save(concert);

        return savedConcert.getId();
    }

}

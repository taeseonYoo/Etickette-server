package com.tae.Etickette.session.application;

import com.tae.Etickette.session.ConcertNotFoundException;
import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.global.model.Seat;
import com.tae.Etickette.session.application.Dto.RegisterSessionReqDto;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SettingSeatService;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.application.VenueNotFoundException;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tae.Etickette.session.application.Dto.RegisterSessionReqDto.*;
import static com.tae.Etickette.session.application.SessionServiceHelper.*;

@Service
@RequiredArgsConstructor
public class RegisterSessionService {
    private final SessionRepository sessionRepository;
    private final VenueRepository venueRepository;
    private final ConcertRepository concertRepository;

    private final SettingSeatService settingSeatService;
    @Transactional
    public void register(RegisterSessionReqDto requestDto) {
        //공연장 중복 확인
        Venue venue = venueRepository.findById(requestDto.getVenueId()).orElseThrow(() ->
                new VenueNotFoundException("공연장을 찾을 수 없습니다."));
        //공연 확인
        Concert concert = concertRepository.findById(requestDto.getConcertId()).orElseThrow(() ->
                new ConcertNotFoundException("공연을 찾을 수 없습니다."));


        //시간 중복 확인
        findExistingDate(sessionRepository, requestDto.getVenueId(), requestDto.getConcertDates());

        //좌석을 세팅한다.
        List<Seat> seatingPlan = settingSeatService.setting(concert.getGradePrices());

        for (SessionInfo sessionInfo : requestDto.getSessionInfos()) {
            Session session = Session.create(sessionInfo.getConcertDate(),
                    sessionInfo.getStartTime(),
                    concert.getRunningTime(),
                    requestDto.getConcertId(),
                    requestDto.getVenueId(),
                    seatingPlan
                    );
            sessionRepository.save(session);
        }
    }




}

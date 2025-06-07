package com.tae.Etickette.session.application;

import com.tae.Etickette.concert.ConcertNotFoundException;
import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.session.application.Dto.RegisterSessionReqDto;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.application.VenueNotFoundException;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.tae.Etickette.session.application.Dto.RegisterSessionReqDto.*;
import static com.tae.Etickette.session.application.SessionServiceHelper.*;

@Service
@RequiredArgsConstructor
public class RegisterSessionService {
    private final SessionRepository sessionRepository;
    private final VenueRepository venueRepository;
    private final ConcertRepository concertRepository;

    @Transactional
    public void register(RegisterSessionReqDto requestDto) {
        //공연장 중복 확인
        Venue venue = venueRepository.findById(requestDto.getVenueId()).orElseThrow(() ->
                new VenueNotFoundException("공연장을 찾을 수 없습니다."));
        //공연 확인
        Concert concert = concertRepository.findById(requestDto.getConcertId()).orElseThrow(() ->
                new ConcertNotFoundException("공연을 찾을 수 없습니다."));

        //시간 중복 확인
        List<LocalDate> requestDates = requestDto.getConcertDates();
        findExistingDate(sessionRepository, requestDto.getVenueId(), requestDates);

        for (SessionInfo sessionInfo : requestDto.getSessionInfos()) {
            Session session = Session.create(sessionInfo.getConcertDate(),
                    sessionInfo.getStartTime(),
                    concert.getRunningTime(),
                    requestDto.getConcertId(),
                    requestDto.getVenueId()
                    );
            sessionRepository.save(session);
        }
    }




}

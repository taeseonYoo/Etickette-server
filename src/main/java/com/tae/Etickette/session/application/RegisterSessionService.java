package com.tae.Etickette.session.application;

import com.tae.Etickette.bookseat.domain.BookSeat;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.seat.infra.SeatRepository;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SettingSeatService;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.command.application.VenueNotFoundException;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.tae.Etickette.session.application.Dto.RegisterSessionRequest.*;
import static com.tae.Etickette.session.application.SessionServiceHelper.*;

@Service
@RequiredArgsConstructor
public class RegisterSessionService {
    private final SessionRepository sessionRepository;
    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final SeatRepository seatRepository;
    private final BookSeatRepository bookSeatRepository;

    private final SettingSeatService settingSeatService;
    @Transactional
    public List<Long> register(RegisterSessionRequest requestDto) {
        //공연장 확인
        venueRepository.findById(requestDto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다."));
        //공연 확인
        Concert concert = concertRepository.findById(requestDto.getConcertId()).orElseThrow(() ->
                new ConcertNotFoundException("공연을 찾을 수 없습니다."));
        //공연에 등록된 좌석 id를 가져온다.
        List<Long> seatIds = seatRepository.findIdByConcertId(concert.getId());
        if (seatIds.isEmpty()) {
            throw new RuntimeException("임시");
        }

        //시간 중복 확인
        findExistingDate(sessionRepository, requestDto.getVenueId(), requestDto.getConcertDates());

        List<Long> sessionIds = new ArrayList<>();
        for (SessionInfo sessionInfo : requestDto.getSessionInfos()) {
            Session session = Session.create(sessionInfo.getConcertDate(),
                    sessionInfo.getStartTime(),
                    concert.getRunningTime(),
                    requestDto.getConcertId(),
                    requestDto.getVenueId()
                    );
            Session savedSession = sessionRepository.save(session);
            sessionIds.add(savedSession.getId());

            List<BookSeat> bookSeats = settingSeatService.setting(seatIds, concert.getGradePrices(), savedSession.getId());
            bookSeatRepository.saveAll(bookSeats);
        }
        return sessionIds;
    }




}

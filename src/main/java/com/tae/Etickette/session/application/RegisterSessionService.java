package com.tae.Etickette.session.application;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.concert.command.application.ConcertFinder;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.seat.application.SeatFinder;
import com.tae.Etickette.seat.infra.SeatRepository;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SettingSeatService;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.command.application.VenueFinder;
import com.tae.Etickette.venue.command.domain.Venue;
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
    private final SeatFinder seatFinder;
    private final VenueFinder venueFinder;
    private final ConcertFinder concertFinder;
    private final SessionRepository sessionRepository;
    private final BookSeatRepository bookSeatRepository;
    private final SettingSeatService settingSeatService;

    @Transactional
    public List<Long> register(RegisterSessionRequest requestDto) {
        Concert concert = concertFinder.findByIdOrThrow(requestDto.getConcertId());
        Venue venue = venueFinder.findVenueByIdOrThrow(concert.getVenueId());
        List<Long> seatIds = seatFinder.findIdsByConcertId(concert.getId());

        //시간 중복 확인
        findExistingDate(sessionRepository, concert.getVenueId(), requestDto.getConcertDates());

        List<Long> sessionIds = new ArrayList<>();
        for (SessionInfo sessionInfo : requestDto.getSessionInfos()) {
            Session session = Session.create(sessionInfo.getConcertDate(),
                    sessionInfo.getStartTime(),
                    concert.getRunningTime(),
                    requestDto.getConcertId()
            );
            Session savedSession = sessionRepository.save(session);
            sessionIds.add(savedSession.getId());

            List<BookSeat> bookSeats = settingSeatService.setting(seatIds, concert.getGradePrices(),
                    savedSession.getId());
            bookSeatRepository.saveAllInBulk(bookSeats);
        }
        return sessionIds;
    }
}

package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.concert.command.application.ConcertFinder;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.session.application.SessionFinder;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.venue.query.VenueData;
import com.tae.Etickette.venue.query.VenueQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertDetailService {
    private final ConcertFinder concertFinder;
    private final SessionFinder sessionFinder;
    private final VenueQueryService venueQueryService;
    public ConcertDetail getConcertDetail(Long concertId) {
        Concert concert = concertFinder.findByIdOrThrow(concertId);

        List<Session> sessions = sessionFinder.findAllByConcertId(concertId);

        List<SessionDetail> sessionDetails = sessions.stream().map(SessionDetail::new).toList();

        VenueData venueData = venueQueryService.findVenueByIdOrThrow(concert.getVenueId());

        return new ConcertDetail(concert, venueData, sessionDetails);
    }
}

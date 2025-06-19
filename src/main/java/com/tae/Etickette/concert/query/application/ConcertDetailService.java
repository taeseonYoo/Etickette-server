package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.query.VenueData;
import com.tae.Etickette.venue.query.VenueQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertDetailService {
    private final ConcertRepository concertRepository;
    private final SessionRepository sessionRepository;
    private final VenueQueryService venueQueryService;
    public Optional<ConcertDetail> getConcertDetail(Long concertId) {
        Optional<Concert> concertOpt = concertRepository.findById(concertId);
        List<Session> sessions = sessionRepository.findAllByConcertId(concertId);

        if (sessions.isEmpty()) return Optional.empty();

        Optional<VenueData> venueData = venueQueryService.getVenue(sessions.get(0).getVenueId());

        return venueData.flatMap(venue -> concertOpt.map(concert -> {
            List<SessionDetail> sessionDetails = sessions.stream()
                    .map(SessionDetail::new)
                    .collect(Collectors.toList());
            return new ConcertDetail(concert, venue, sessionDetails);
        }));

    }
}

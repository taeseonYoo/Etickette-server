package com.tae.Etickette;


import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.command.domain.GradePrice;
import com.tae.Etickette.concert.command.domain.Image;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestInitializer {

    private final VenueRepository venueRepository;
    private final ConcertRepository concertRepository;
    private final SessionRepository sessionRepository;

    @PostConstruct
    public void init() {
        Venue venue = Venue.create("공연장1", 10000, new Address("서울시", "잠실", "11234"));
        Venue savedVenue = venueRepository.save(venue);

        for (int i = 0; i < 21; i++) {
            Image image = new Image("https://tae-s3-1.s3.ap-northeast-2.amazonaws.com/concert" + ((i%10)+1) + ".gif");
            List<GradePrice> gradePrices = List.of(new GradePrice("VIP", new Money(120000)));
            Concert concert = Concert.create("공연" + (i + 1), "공연 미리보기", 120, image, gradePrices, savedVenue.getId());
            Concert savedConcert = concertRepository.save(concert);

            Session session = Session.create(LocalDate.of(2025, 7, i + 1), LocalTime.of(5, 3), 120, savedConcert.getId());
            sessionRepository.save(session);
        }
    }

}

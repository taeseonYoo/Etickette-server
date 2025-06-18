package com.tae.Etickette;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.GradePrice;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.query.application.ConcertQueryService;
import com.tae.Etickette.concert.query.dao.ConcertSummaryDao;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private VenueRepository venueRepository;

    @PostConstruct
    public void a() {
        Venue savedVenue = venueRepository.save(Venue.create("KSPO", 10000, new Address("서울", "강남", "11111")));
        Concert savedConcert = concertRepository.save(Concert.create("공연1", "공연입니다.", 120, "", List.of(new GradePrice("VIP", new Money(100)))));
        Session savedSession = sessionRepository.save(Session.create(LocalDate.of(2025, 6, 2), LocalTime.of(15, 0), 120, savedConcert.getId(), savedVenue.getId()));
    }
}

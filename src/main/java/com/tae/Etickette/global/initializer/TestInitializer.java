package com.tae.Etickette.global.initializer;


import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.command.domain.GradePrice;
import com.tae.Etickette.concert.command.domain.Image;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.member.application.MemberService;
import com.tae.Etickette.member.application.dto.RegisterMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberResponse;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.seat.domain.Seat;
import com.tae.Etickette.seat.infra.SeatRepository;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
public class TestInitializer {

    private final VenueRepository venueRepository;
    private final ConcertRepository concertRepository;
    private final MemberService memberService;
    private final SeatRepository seatRepository;
    private final RegisterSessionService sessionService;

    List<String> title = new ArrayList<>();
    @PostConstruct
    public void init() {
        title.add("강다니엘");
        title.add("오아시스");
        title.add("박보검");
        title.add("키스오브라이프");
        title.add("henry");
        title.add("asia top artist");
        title.add("찰리푸스");
        title.add("paradigm");
        title.add("플레이브");
        title.add("touched concert");

        for (int i = 0; i < 10; i++) {
            Venue venue = Venue.create("KSPO DOME" + (i + 1), 10000, new Address("서울시", "잠실", "" + (i + 1)));
            Venue savedVenue = venueRepository.save(venue);

            for (int j = 0; j < 10; j++) {
                Image image = new Image("https://tae-s3-1.s3.ap-northeast-2.amazonaws.com/concert" + ((j%10)+1) + ".gif");
                List<GradePrice> gradePrices = List.of(new GradePrice("VIP", new Money(120000)), new GradePrice("S", new Money(100000)), new GradePrice("R", new Money(80000)));
                Concert concert = Concert.create(title.get(j), "공연 미리보기", 120, image, gradePrices, savedVenue.getId());
                Concert savedConcert = concertRepository.save(concert);
                List<Seat> seats = initSeat(savedConcert.getId());
                seatRepository.saveAllInBulk(seats);

                RegisterSessionRequest sessionRequest = RegisterSessionRequest.builder()
                        .concertId(savedConcert.getId()).sessionInfos(List.of(RegisterSessionRequest.SessionInfo.builder().concertDate(LocalDate.of(2025, 7, j + 1)).startTime(LocalTime.of(5, 3)).build())).build();
                sessionService.register(sessionRequest);
            }
        }

        RegisterMemberRequest admin = RegisterMemberRequest.builder().name("관리자").password("#ACb1234").email("admin@spring").build();
        memberService.register(admin);
        memberService.adminRegister("admin@spring");


    }

    private List<Seat> initSeat(Long concertId) {
        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('A' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('F' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        for (int i = 1; i <= 50; i++) {
            seats.add(Seat.create(String.valueOf((char) ('K' + (i - 1) / 10)), String.valueOf((char) ((i - 1) % 10) + 1), concertId));
        }
        return seats;
    }

}

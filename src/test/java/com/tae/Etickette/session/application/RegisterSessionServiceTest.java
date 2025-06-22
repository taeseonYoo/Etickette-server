package com.tae.Etickette.session.application;

import com.tae.Etickette.seat.infra.SeatRepository;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SettingSeatService;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.command.application.VenueNotFoundException;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.tae.Etickette.session.application.Dto.RegisterSessionRequest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - ChangeVenueService")
class RegisterSessionServiceTest {
    @InjectMocks
    private RegisterSessionService registerSessionService;
    private final SessionRepository sessionRepository = mock(SessionRepository.class);
    private final VenueRepository venueRepository = mock(VenueRepository.class);
    private final ConcertRepository concertRepository = mock(ConcertRepository.class);
    private final SeatRepository seatRepository = mock(SeatRepository.class);
    private final SettingSeatService settingSeatService = mock(SettingSeatService.class);
    private final BookSeatRepository bookSeatRepository = mock(BookSeatRepository.class);

    RegisterSessionRequest requestDto;
    @BeforeEach
    void setUp() {
        registerSessionService = new RegisterSessionService(sessionRepository,concertRepository,venueRepository,seatRepository, bookSeatRepository,settingSeatService);

        List<SessionInfo> sessionInfos = List.of(SessionInfo.builder().concertDate(LocalDate.of(2025, 6, 1))
                        .startTime(LocalTime.of(15, 0)).build(),
                SessionInfo.builder().concertDate(LocalDate.of(2025, 6, 2))
                        .startTime(LocalTime.of(15, 0)).build());

        requestDto = builder()
                .concertId(1L)
                .sessionInfos(sessionInfos).build();
    }

    @Test
    @DisplayName("register - 세션 등록에 성공한다.")
    void 세션등록_성공() {
        //given
        BDDMockito.given(concertRepository.findById(any())).willReturn(Optional.of(mock(Concert.class)));
        BDDMockito.given(seatRepository.findIdByConcertId(any())).willReturn(mock(List.class));
        BDDMockito.given(venueRepository.findById(any())).willReturn(Optional.of(mock(Venue.class)));

        BDDMockito.given(sessionRepository.save(any())).willReturn(mock(Session.class));
        //when
        registerSessionService.register(requestDto);

        //then
        BDDMockito.then(sessionRepository).should(atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("register - 공연장이 없다면, 세션 등록에 실패한다.")
    void 세션등록_실패_공연장이없음() {
        //given
        BDDMockito.given(concertRepository.findById(any())).willReturn(Optional.of(mock(Concert.class)));
        BDDMockito.given(venueRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        Assertions.assertThrows(VenueNotFoundException.class, () ->
                registerSessionService.register(requestDto));
    }

    @Test
    @DisplayName("register - 공연이 없다면, 세션 등록에 실패한다.")
    void 세션등록_실패_공연이없음() {
        //given
        BDDMockito.given(concertRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        Assertions.assertThrows(ConcertNotFoundException.class, () ->
                registerSessionService.register(requestDto));
    }

}
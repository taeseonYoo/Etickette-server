package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.concert.command.application.ConcertFinder;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.command.domain.Image;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.session.application.SessionFinder;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.venue.query.VenueData;
import com.tae.Etickette.venue.query.VenueQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - ConcertDetailService")
class ConcertDetailServiceTest {
    @InjectMocks
    private ConcertDetailService concertDetailService;
    private final ConcertFinder concertFinder = mock(ConcertFinder.class);
    private final SessionFinder sessionFinder = mock(SessionFinder.class);
    private final VenueQueryService venueQueryService = mock(VenueQueryService.class);


    @Test
    @DisplayName("상세정보 - 공연 정보를 찾을 수 없다면, 빈 값을 반환한다.")
    void 상세정보_공연정보가없음() {
        //given
        BDDMockito.given(concertFinder.findByIdOrThrow(any())).willThrow(ResourceNotFoundException.class);

        //when
        Assertions.assertThatThrownBy(() -> concertDetailService.getConcertDetail(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("상세정보 - 세션 정보를 찾을 수 없다면, 빈 값을 반환한다.")
    void 상세정보_세션정보가없음() {
        //given
        BDDMockito.given(concertFinder.findByIdOrThrow(any())).willReturn(mock(Concert.class));
        BDDMockito.given(sessionFinder.findAllByConcertId(any())).willThrow(ResourceNotFoundException.class);
        //when
        Assertions.assertThatThrownBy(() -> concertDetailService.getConcertDetail(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("상세정보 - 공연장 정보를 찾을 수 없다면, 빈 값을 반환한다.")
    void 상세정보_공연장정보가없음() {
        //given
        BDDMockito.given(concertFinder.findByIdOrThrow(any())).willReturn(mock(Concert.class));
        BDDMockito.given(sessionFinder.findAllByConcertId(any())).willReturn(List.of(mock(Session.class)));
        BDDMockito.given(venueQueryService.findVenueByIdOrThrow(any())).willThrow(ResourceNotFoundException.class);
        //when
        //then
        Assertions.assertThatThrownBy(() -> concertDetailService.getConcertDetail(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("상세정보 - 모든 데이터가 존재하면, 상세정보를 반환한다.")
    void 상세정보_반환성공() {
        //given
        Concert concert = Concert.create("title", "overview", 120, new Image("/imagePath"), List.of(), 1L);
        BDDMockito.given(concertFinder.findByIdOrThrow(any())).willReturn(concert);
        BDDMockito.given(sessionFinder.findAllByConcertId(any())).willReturn(List.of(mock(Session.class)));
        BDDMockito.given(venueQueryService.findVenueByIdOrThrow(any())).willReturn(mock(VenueData.class));
        //when
        ConcertDetail concertDetail = concertDetailService.getConcertDetail(1L);

        //then
        assertNotNull(concertDetail);
    }

}
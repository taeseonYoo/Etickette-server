package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - ConcertService")
class ConcertServiceTest {

    @InjectMocks
    private ConcertService concertService;
    private final ConcertRepository mockConcertRepo = mock(ConcertRepository.class);
    private final VenueRepository mockVenueRepo = mock(VenueRepository.class);

    @BeforeEach
    void setUp() {
        concertService = new ConcertService(mockConcertRepo, mockVenueRepo);
    }

    @Test
    @DisplayName("createConcert - 콘서트 생성에 성공한다.")
    void 콘서트생성_성공() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        ConcertCreateRequestDto requestDto = ConcertCreateRequestDto.builder()
                .venueId(venue.getId())
                .title("Test Title")
                .overview("Test Overview")
                .runningTime(120)
                .imgUrl("img.png")
                .scheduleInfos(List.of(new ConcertCreateRequestDto.ScheduleInfo(LocalDate.now(), LocalTime.of(19, 0))))
                .sectionInfos(List.of(new ConcertCreateRequestDto.SectionInfo("VIP", 150000)))
                .build();
        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgUrl(),
                venue,
                requestDto.toSectionEntities(),
                requestDto.toScheduleEntities());
        ReflectionTestUtils.setField(concert, "id", 1L);

        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));
        BDDMockito.given(mockConcertRepo.save(any()))
                .willReturn(concert);

        //when
        concertService.createConcert(requestDto);

        //then
        verify(mockConcertRepo).save(any(Concert.class));
    }

    @Test
    @DisplayName("createConcert - 공연장을 찾을 수 없다면,콘서트 생성에 실패한다.")
    void 콘서트생성_실패_공연장없음() {
        //given
        ConcertCreateRequestDto requestDto = ConcertCreateRequestDto.builder()
                .venueId(1L)
                .title("Test Title")
                .overview("Test Overview")
                .runningTime(120)
                .imgUrl("img.png")
                .scheduleInfos(List.of(new ConcertCreateRequestDto.ScheduleInfo(LocalDate.now(), LocalTime.of(19, 0))))
                .sectionInfos(List.of(new ConcertCreateRequestDto.SectionInfo("VIP", 150000)))
                .build();

        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.empty());

        //when & then
        Assertions.assertThrows(VenueNotFoundException.class,
                () -> concertService.createConcert(requestDto));
    }
}
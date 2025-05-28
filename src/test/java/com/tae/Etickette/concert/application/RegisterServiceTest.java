package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import com.tae.Etickette.schedule.domain.Schedule;
import com.tae.Etickette.schedule.infra.ScheduleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.tae.Etickette.concert.application.ConcertCreateRequestDto.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - ConcertService")
class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;
    private final ConcertRepository mockConcertRepo = mock(ConcertRepository.class);
    private final VenueRepository mockVenueRepo = mock(VenueRepository.class);
    private final ScheduleRepository mockScheduleRepo = mock(ScheduleRepository.class);

    @BeforeEach
    void setUp() {
        registerService = new RegisterService(mockConcertRepo, mockVenueRepo, mockScheduleRepo);
    }

    @Test
    @DisplayName("createConcert - 콘서트 생성에 성공한다.")
    void 콘서트생성_성공() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(venue.getId(), scheduleInfos);

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgUrl(),
                venue.getId(),
                requestDto.toSectionEntities());
        ReflectionTestUtils.setField(concert, "id", 1L);

        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));
        BDDMockito.given(mockConcertRepo.save(any()))
                .willReturn(concert);

        //when
        registerService.createConcert(requestDto);

        //then
        verify(mockConcertRepo).save(any(Concert.class));
    }

    @Test
    @DisplayName("createConcert - 공연장을 찾을 수 없다면,콘서트 생성에 실패한다.")
    void 콘서트생성_실패_공연장없음() {
        //given
        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(1L, scheduleInfos);

        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.empty());

        //when & then
        Assertions.assertThrows(VenueNotFoundException.class,
                () -> registerService.createConcert(requestDto));
    }

    @Test
    @DisplayName("createConcert - 같은 공연장에 공연 날짜가 같은 스케줄이 있다면, 콘서트 생성에 실패한다.")
    void 콘서트생성_실패_중복_날짜고정1() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(venue.getId(), scheduleInfos);

        List<Schedule> schedules = List.of(Schedule.create(LocalDate.of(2025, 6, 1)
                , LocalTime.of(19, 1), 120, 1L));
        BDDMockito.given(mockScheduleRepo.findByConcertDateAndVenueId(any(), any()))
                .willReturn(schedules);
        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(ScheduleDuplicateException.class,
                () -> registerService.createConcert(requestDto));
    }

    @Test
    @DisplayName("createConcert - 같은 공연장에 공연 날짜가 같은 스케줄이 있다면, 콘서트 생성에 실패한다.")
    void 콘서트생성_실패_중복_날짜고정2() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(venue.getId(), scheduleInfos);

        List<Schedule> schedules = List.of(Schedule.create(LocalDate.of(2025, 6, 1)
                , LocalTime.of(0, 0), 120, 1L));
        BDDMockito.given(mockScheduleRepo.findByConcertDateAndVenueId(any(), any()))
                .willReturn(schedules);
        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(ScheduleDuplicateException.class,
                () -> registerService.createConcert(requestDto));
    }

    @Test
    @DisplayName("createConcert - 같은 공연장에 공연 날짜가 같은 스케줄이 있다면, 콘서트 생성에 실패한다.")
    void 콘서트생성_실패_중복_날짜고정3() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(venue.getId(), scheduleInfos);

        List<Schedule> schedules = List.of(Schedule.create(LocalDate.of(2025, 6, 1)
                , LocalTime.of(23, 59), 120, 1L));
        BDDMockito.given(mockScheduleRepo.findByConcertDateAndVenueId(any(), any()))
                .willReturn(schedules);
        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(ScheduleDuplicateException.class,
                () -> registerService.createConcert(requestDto));
    }
    @Test
    @DisplayName("createConcert - 같은 공연장에 공연 날짜가 같은 스케줄이 있다면, 콘서트 생성에 실패한다.")
    void 콘서트생성_실패_중복시간_시간고정1() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(venue.getId(), scheduleInfos);

        List<Schedule> schedules = List.of(Schedule.create(LocalDate.of(2024, 6, 1)
                , LocalTime.of(19, 0), 120, 1L));
        BDDMockito.given(mockScheduleRepo.findByConcertDateAndVenueId(any(), any()))
                .willReturn(schedules);
        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(ScheduleDuplicateException.class,
                () -> registerService.createConcert(requestDto));
    }

    @Test
    @DisplayName("createConcert - 같은 공연장에 공연 날짜가 같은 스케줄이 있다면, 콘서트 생성에 실패한다.")
    void 콘서트생성_실패_중복시간_시간고정2() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(venue.getId(), scheduleInfos);

        List<Schedule> schedules = List.of(Schedule.create(LocalDate.of(2025, 5, 1)
                , LocalTime.of(19, 0), 120, 1L));

        BDDMockito.given(mockScheduleRepo.findByConcertDateAndVenueId(any(), any()))
                .willReturn(schedules);
        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(ScheduleDuplicateException.class,
                () -> registerService.createConcert(requestDto));
    }
    @Test
    @DisplayName("createConcert - 같은 공연장에 공연 날짜가 같은 스케줄이 있다면, 콘서트 생성에 실패한다.")
    void 콘서트생성_실패_중복시간_시간고정3() {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "강남", "11111"));
        ReflectionTestUtils.setField(venue, "id", 1L);

        List<ScheduleInfo> scheduleInfos = List.of(new ScheduleInfo(LocalDate.of(2025, 6, 1),
                LocalTime.of(19, 0)));

        ConcertCreateRequestDto requestDto = create(venue.getId(), scheduleInfos);

        List<Schedule> schedules = List.of(Schedule.create(LocalDate.of(2025, 6, 2)
                , LocalTime.of(19, 0), 120, 1L));

        BDDMockito.given(mockScheduleRepo.findByConcertDateAndVenueId(any(), any()))
                .willReturn(schedules);
        BDDMockito.given(mockVenueRepo.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(ScheduleDuplicateException.class,
                () -> registerService.createConcert(requestDto));
    }

    private ConcertCreateRequestDto create(Long venueId, List<ScheduleInfo> scheduleInfos) {
        return ConcertCreateRequestDto.builder()
                .venueId(venueId)
                .title("Test Title")
                .overview("Test Overview")
                .runningTime(120)
                .imgUrl("img.png")
                .scheduleInfos(scheduleInfos)
                .sectionInfos(List.of(new SectionInfo("VIP", 150000)))
                .build();
    }

}
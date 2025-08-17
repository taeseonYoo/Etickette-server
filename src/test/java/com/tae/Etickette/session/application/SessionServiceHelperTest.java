package com.tae.Etickette.session.application;

import com.tae.Etickette.global.exception.ConflictException;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - SessionServiceHelper")
class SessionServiceHelperTest {
    private final SessionRepository sessionRepository = mock(SessionRepository.class);

    @Test
    @DisplayName("중복되는 시간이 없다면, 검증에 성공한다.")
    void findExistingDate_성공() {
        //given
        List<LocalDate> requestDates = List.of(LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 2),
                LocalDate.of(2025, 6, 3));

        List<Session> savedSessions = List.of(
                Session.create(LocalDate.of(2025, 5, 1), LocalTime.of(15, 0), 120, 1L),
                Session.create(LocalDate.of(2025, 5, 2), LocalTime.of(15, 0), 120, 1L)
        );

        BDDMockito.given(sessionRepository.findAllByVenueId(any())).willReturn(savedSessions);

        //when & then
        assertDoesNotThrow(()->SessionServiceHelper.findExistingDate(sessionRepository,1L,requestDates));
    }

    @Test
    @DisplayName("중복되는 날짜가 존재하면, AlreadyExistingDate 에러가 발생한다.")
    void findExistingDate_실패_중복존재() {
        //given
        List<LocalDate> requestDates = List.of(LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 2),
                LocalDate.of(2025, 6, 3));

        List<Session> savedSessions = List.of(
                Session.create(LocalDate.of(2025, 5, 1), LocalTime.of(15, 0), 120, 1L),
                Session.create(LocalDate.of(2025, 6, 2), LocalTime.of(15, 0), 120, 1L)
        );

        BDDMockito.given(sessionRepository.findAllByVenueId(any())).willReturn(savedSessions);

        //when & then
        assertThrows(ConflictException.class,
                ()->SessionServiceHelper.findExistingDate(sessionRepository,1L,requestDates));
    }

    @Test
    @DisplayName("공연장 스케줄이 없다면, 검증에 성공한다.")
    void findExistingDate_실패_스케줄없음() {
        //given
        List<LocalDate> requestDates = List.of(LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 2),
                LocalDate.of(2025, 6, 3));

        BDDMockito.given(sessionRepository.findAllByVenueId(any())).willReturn(List.of());

        //when & then
        assertDoesNotThrow(()->SessionServiceHelper.findExistingDate(sessionRepository,1L,requestDates));
    }
}
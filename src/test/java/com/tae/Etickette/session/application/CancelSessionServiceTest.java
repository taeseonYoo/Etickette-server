package com.tae.Etickette.session.application;

import com.tae.Etickette.booking.application.SessionNotFoundException;
import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SessionCanceledEvent;
import com.tae.Etickette.session.domain.SessionStatus;
import com.tae.Etickette.session.infra.SessionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - CancelSessionService")
class CancelSessionServiceTest {

    @InjectMocks
    CancelSessionService cancelSessionService;
    private final SessionRepository sessionRepository = mock(SessionRepository.class);

    @Test
    @DisplayName("cancel - 세션 정보가 없으면,세션 취소에 실패한다.")
    void cancel_실패_세션이없음() {
        //given
        BDDMockito.given(sessionRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThrows(SessionNotFoundException.class, () ->
                cancelSessionService.cancel(1L));
    }

    @Test
    @DisplayName("cancel - 취소에 성공하면 , session.cancel()이 호출 되어야한다.")
    void cancel_성공() {
        //given
        Session session = mock(Session.class);
        BDDMockito.given(sessionRepository.findById(any())).willReturn(Optional.of(session));

        //when
        cancelSessionService.cancel(session.getId());

        //then
        verify(session).cancel();
    }
}
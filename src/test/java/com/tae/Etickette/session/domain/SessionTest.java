package com.tae.Etickette.session.domain;

import com.tae.Etickette.global.event.Events;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - Session")
class SessionTest {
    private final ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    void setUp() {
        Events.setPublisher(publisher);
    }

    @Test
    @DisplayName("세션이 취소되면 SessionStatus.CANCELED")
    void status_canceled() {
        //given
        Session session = Session.create(LocalDate.of(2025, 6, 1), LocalTime.of(15, 0), 120, 1L);
        //when
        session.cancel();
        //then
        Assertions.assertThat(session.getStatus()).isEqualTo(SessionStatus.CANCELED);
    }

    @Test
    @DisplayName("cancel - 세션이 취소되면 SessionCanceledEvent 이벤트가 발행된다.")
    void event_발행() {
        //given
        Session session = Session.create(LocalDate.of(2025, 6, 1), LocalTime.of(15, 0), 120, 1L);
        //when
        session.cancel();
        //then
        verify(publisher).publishEvent(any(SessionCanceledEvent.class));
    }

    @Test
    @DisplayName("cancel - 이미 취소된 세션은 취소할 수 없다.")
    void 취소된세션_취소실패() {
        //given
        Session session = Session.create(LocalDate.of(2025, 6, 1), LocalTime.of(15, 0), 120, 1L);
        //when
        session.cancel();
        //then
        assertThrows(AlreadyStartedException.class, () ->
                session.cancel());
    }
}
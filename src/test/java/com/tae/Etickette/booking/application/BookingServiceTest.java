package com.tae.Etickette.booking.application;

import com.tae.Etickette.booking.application.dto.BookingRequest;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.session.infra.SessionRepository;
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
@DisplayName("Unit - BookingService")
class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final SessionRepository sessionRepository = mock(SessionRepository.class);
    private final BookSeatRepository bookSeatRepository = mock(BookSeatRepository.class);

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(sessionRepository, bookingRepository, bookSeatRepository);
    }

    @Test
    @DisplayName("booking - 세션 정보가 없다면, 예약에 실패한다.")
    void 예약_실패_세션이없음(){
        //given
        BookingRequest request = BookingRequest.builder().memberId(1L).sessionId(1L).seatIds(List.of(1L, 2L)).build();
        BDDMockito.given(sessionRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThrows(SessionNotFoundException.class,
                () -> bookingService.booking(request));
    }

    @Test
    @DisplayName("booking - 좌석 정보가 없다면, 예약에 실패한다.")
    void 예약_실패_예약좌석이없음(){
        //given
        BookingRequest request = BookingRequest.builder().memberId(1L).sessionId(1L).seatIds(List.of(1L, 2L)).build();
        BDDMockito.given(sessionRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThrows(SessionNotFoundException.class,
                () -> bookingService.booking(request));
    }
}
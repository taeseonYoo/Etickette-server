package com.tae.Etickette.bookseat.command.application;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - BookingService")
class BookSeatServiceTest {
    @InjectMocks
    BookSeatService bookSeatService;
    BookSeatRepository bookSeatRepository = mock(BookSeatRepository.class);

    @BeforeEach
    void setUp() {
        bookSeatService = new BookSeatService(bookSeatRepository);
    }

    @Test
    @DisplayName("좌석 예약에 성공하면, reserve가 호출되어야 한다.")
    void book_success() {
        //given
        BookSeat mock = mock(BookSeat.class);
        mock.lock();
        BDDMockito.given(bookSeatRepository.findById(new BookSeatId(1L, 1L)))
                .willReturn(Optional.of(mock));
        //when
        bookSeatService.book(new BookSeatId(1L, 1L));

        //then
        verify(mock, times(1)).reserve();
    }
    @Test
    @DisplayName("좌석을 찾을 수 없으면, ResourceNotFoundException 예외가 발생한다.")
    void book_fail() {
        //given
        BDDMockito.given(bookSeatRepository.findById(new BookSeatId(1L, 1L)))
                .willReturn(Optional.empty());
        //when
        Assertions.assertThatThrownBy(() ->
                        bookSeatService.book(new BookSeatId(1L, 1L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKSEAT_NOT_FOUND);
    }
}
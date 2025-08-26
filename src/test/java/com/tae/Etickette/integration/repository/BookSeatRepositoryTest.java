package com.tae.Etickette.integration.repository;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.SeatStatus;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.global.model.Money;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;


@DataJpaTest
@DisplayName("Integration - BookSeatRepository")
@Transactional
class BookSeatRepositoryTest {
    @Autowired
    BookSeatRepository bookSeatRepository;

    @Test
    @DisplayName("100개의 데이터를 벌크연산으로 저장하면, 100개 모두 저장되어야 한다.")
    void 벌크_연산_성공() {
        //given
        List<BookSeat> seats = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> BookSeat.create((long) i, 1L, "VIP", new Money(100000)))
                .toList();

        //when
        bookSeatRepository.saveAllInBulk(seats);

        //then
        List<BookSeat> findSeats = bookSeatRepository.findAll();
        Assertions.assertThat(findSeats.size()).isEqualTo(100);
    }
    @Test
    @DisplayName("saveAllInBulk - 벌크 연산으로 데이터를 저장하면, 값이 모두 제대로 저장되어야 한다.")
    void 벌크연산_성공_값검증() {
        //given
        List<BookSeat> bookSeats = List.of(BookSeat.create(1L, 1L, "VIP", new Money(100000)));

        //when
        bookSeatRepository.saveAllInBulk(bookSeats);

        //then
        BookSeat findSeat = bookSeatRepository.findAll().get(0);
        Assertions.assertThat(findSeat)
                .usingRecursiveComparison()
                .isEqualTo(BookSeat.create(1L, 1L, "VIP", new Money(100000)));
    }

    @Test
    @DisplayName("중복 키를 저장하면, DuplicateKeyException가 발생한다.")
    void 중복_키_예외() {
        //given
        List<BookSeat> bookSeats = List.of(
                BookSeat.create(1L, 1L, "VIP", new Money(100000)),
                BookSeat.create(1L, 1L, "S", new Money(80000))
        );
        //when & then
        Assertions.assertThatThrownBy(() ->
                bookSeatRepository.saveAllInBulk(bookSeats))
                .isInstanceOf(DuplicateKeyException.class);
    }
    @Test
    @DisplayName("벌크 연산 사용 시, 빈 리스트를 넣으면 아무 일도 일어나지 않는다.")
    void 빈_배열_저장() {
        //given
        List<BookSeat> bookSeats = Collections.emptyList();

        //when
        bookSeatRepository.saveAllInBulk(bookSeats);

        //then
        List<BookSeat> savedSeats = bookSeatRepository.findAll();
        Assertions.assertThat(savedSeats).isEmpty();
    }
}
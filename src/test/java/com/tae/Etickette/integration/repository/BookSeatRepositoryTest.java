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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


@DataJpaTest
@DisplayName("Integration - BookSeatRepository")
@Transactional
class BookSeatRepositoryTest {
    @Autowired
    BookSeatRepository bookSeatRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("saveAllInBulk - 100개의 데이터를 넣으면, 100개 모두 저장되어야 한다.")
    void 여러_좌석_저장성공() {
        //given
        List<BookSeat> seats = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> BookSeat.create((long) i, 1L, "VIP", new Money(100000)))
                .toList();

        //when
        bookSeatRepository.saveAllInBulk(seats);
        em.flush();

        //then
        List<BookSeat> findSeats = bookSeatRepository.findAll();
        Assertions.assertThat(findSeats.size()).isEqualTo(100);
    }
    @Test
    @DisplayName("saveAllInBulk - 벌크 연산으로 데이터를 저장하면, 값이 모두 제대로 저장되어야 한다.")
    void 좌석_저장성공_값검증() {
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

}
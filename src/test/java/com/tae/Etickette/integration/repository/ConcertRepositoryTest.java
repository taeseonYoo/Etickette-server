package com.tae.Etickette.integration.repository;

import com.tae.Etickette.Concert.entity.Concert;
import com.tae.Etickette.Concert.repository.ConcertRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class ConcertRepositoryTest {
    @Autowired
    ConcertRepository concertRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void 콘서트_생성() {
        //given
        Concert concert = Concert.create("2024 IU H．E．R．WORLD TOUR CONCERT IN SEOUL",
                "아이유 24년 공연",
                LocalDate.of(2024, 3, 2),
                LocalDate.of(2024, 3, 10));
        //when
        Concert savedConcert = concertRepository.save(concert);

        //then
        Assertions.assertThat(savedConcert).isEqualTo(concert);
    }
}

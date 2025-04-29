package com.tae.Etickette.integration.repository;

import com.tae.Etickette.Concert.entity.Concert;
import com.tae.Etickette.Concert.repository.ConcertRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ConcertRepositoryTest {
    @Autowired
    ConcertRepository concertRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("콘서트 생성 - 콘서트 생성에 성공한다.")
    public void 콘서트_생성_성공() {
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

    @Test
    @DisplayName("콘서트 수정 - 콘서트 수정에 성공한다.")
    public void 콘서트_수정_성공() {
        //given
        Concert concert = Concert.create("2024 IU H．E．R．WORLD TOUR CONCERT IN SEOUL",
                "아이유 24년 공연",
                LocalDate.of(2024, 3, 2),
                LocalDate.of(2024, 3, 10));
        Concert savedConcert = concertRepository.save(concert);

        //when
        Concert findConcert = concertRepository.findById(savedConcert.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 콘서트가 없습니다."));
        findConcert.updateTitle("2024 IU H．E．R．WORLD TOUR CONCERT IN BUSAN");
        em.flush();

        //then
        Concert findUpdateConcert = concertRepository.findById(savedConcert.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 콘서트가 없습니다."));
        Assertions.assertThat(findUpdateConcert.getTitle()).isEqualTo("2024 IU H．E．R．WORLD TOUR CONCERT IN BUSAN");
    }

    @Test
    @DisplayName("콘서트 삭제 - 콘서트 삭제에 성공한다.")
    public void 콘서트_삭제_성공() {
        //given
        Concert concert = Concert.create("2024 IU H．E．R．WORLD TOUR CONCERT IN SEOUL",
                "아이유 24년 공연",
                LocalDate.of(2024, 3, 2),
                LocalDate.of(2024, 3, 10));
        Concert savedConcert = concertRepository.save(concert);

        //when
        concertRepository.delete(savedConcert);

        //then
        assertThrows(RuntimeException.class,
                () -> concertRepository.findById(savedConcert.getId()).orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."))
        );
    }
}

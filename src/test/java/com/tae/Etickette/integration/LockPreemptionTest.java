package com.tae.Etickette.integration;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.global.model.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;


@SpringBootTest
@DisplayName("Integration - PESSIMISTIC_WRITE")
public class LockPreemptionTest {
    @Autowired
    BookSeatRepository bookSeatRepository;
    @Autowired
    TransactionTemplate transactionTemplate;


    @Test
    @DisplayName("비관적락 - 락 선점 시, 대기 시간이 길어지면 타임아웃으로 인한 예외 발생")
    void 비관적락_타임아웃() throws InterruptedException{
        bookSeatRepository.save(BookSeat.create(1L, 1L, "A", new Money(100000)));
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        AtomicReference<Throwable> error = new AtomicReference<>();

        // A 스레드
        executorService.submit(() -> {
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    bookSeatRepository.findByIdWithLock(1L, 1L);
                    System.out.println("A: 락 획득");
                    latch.countDown(); // 락을 획득했음을 B에게 알린다.
                    Thread.sleep(11000); // 락 유지
                    System.out.println("A: 작업 완료");
                } catch (Exception e) {
                    System.out.println("A: 실패 - " + e.getClass().getName());
                }
            });
        });

        latch.await();
        // B 스레드: 락 걸린 상태에서 조회 시도
        executorService.submit(() -> {
            System.out.println("B: 조회 시도");
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    bookSeatRepository.findByIdWithLock(1L, 1L);
                    System.out.println("B: 조회 성공");
                } catch (Exception e) {
                    error.set(e);
                    System.out.println("B: 조회 실패 - " + e.getClass().getName() + " - " + e.getMessage());
                }
            });
        });

        executorService.shutdown();
        executorService.awaitTermination(15, TimeUnit.SECONDS);

        Assertions.assertThat(error.get()).isInstanceOf(PessimisticLockingFailureException.class);
    }

    @Test
    @DisplayName("비관적락 - 락 선점 중, findById를 사용한 일반 조회는 정상 동작한다.")
    void 락_일반조회_성공() throws InterruptedException{
        //given
        bookSeatRepository.save(BookSeat.create(2L, 2L, "A", new Money(100000)));
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        AtomicReference<Throwable> error = new AtomicReference<>();

        //when
        // A 스레드
        executorService.submit(() -> {
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    bookSeatRepository.findByIdWithLock(2L, 2L);
                    System.out.println("A: 락 획득");
                    latch.countDown(); // 락을 획득했음을 B에게 알린다.
                    Thread.sleep(5000); // 락 유지
                    System.out.println("A: 작업 완료");
                } catch (Exception e) {
                    System.out.println("A: 실패 - " + e.getClass().getName());
                }
            });
        });

        latch.await();
        // B 스레드: 락 걸린 상태에서 조회 시도
        executorService.submit(() -> {
            System.out.println("B: 조회 시도");
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    bookSeatRepository.findById(new BookSeatId(2L, 2L));
                    System.out.println("B: 조회 성공");
                } catch (Exception e) {
                    error.set(e);
                    System.out.println("B: 조회 실패 - " + e.getClass().getName() + " - " + e.getMessage());
                }
            });
        });

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        //then
        Assertions.assertThat(error.get()).isNull();
    }

}

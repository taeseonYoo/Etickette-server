package com.tae.Etickette.concert.command.domain;

import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.model.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("Unit - Concert")
class ConcertTest {
    private final ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    void setUp() {
        Events.setPublisher(publisher);
    }

    @Test
    @DisplayName("공연 생성에 성공하면, status == READY")
    void 공연생성_성공() {
        //given & when
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);

        //then
        Assertions.assertThat(concert.getStatus()).isEqualTo(ConcertStatus.READY);
    }

    /**
     * 공연 오픈
     */

    @Test
    @DisplayName("예매 준비 공연은 예매 오픈할 수 있다.")
    void 공연오픈_성공() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);

        //when
        concert.open();

        //then
        Assertions.assertThat(concert.getStatus()).isEqualTo(ConcertStatus.OPEN);
    }
    @Test
    @DisplayName("예매 오픈 공연은 예매 오픈할 수 없다.")
    void 공연오픈_실패_OPEN() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();

        //when & then
        assertThrows(ConcertNotReadyException.class,
                () -> concert.open());
    }
    @Test
    @DisplayName("예매 마감된 공연은, 예매 오픈이 불가능하다.")
    void 공연오픈_실패_CLOSED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();

        //when & then
        assertThrows(ConcertNotReadyException.class,
                () -> concert.open());
    }
    @Test
    @DisplayName("취소된 공연은, 예매 오픈이 불가능하다.")
    void 공연오픈_실패_CANCELED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.cancel();

        //when & then
        assertThrows(ConcertNotReadyException.class,
                () -> concert.open());
    }
    @Test
    @DisplayName("종료된 공연은, 예매 오픈이 불가능하다.")
    void 공연오픈_실패_FINISHED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();
        concert.finish();

        //when & then
        assertThrows(ConcertNotReadyException.class,
                () -> concert.open());
    }

    /**
     * 공연 예매 마감
     */
    @Test
    @DisplayName("예매 오픈된 공연은 예매를 마감할 수 있다.")
    void 공연예매마감_성공() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();

        //when
        concert.close();

        //then
        Assertions.assertThat(concert.getStatus()).isEqualTo(ConcertStatus.SALE_CLOSED);
    }
    @Test
    @DisplayName("예매 준비 공연은, 예매 마감에 실패한다.")
    void 공연예매마감_실패_READY() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);

        //when
        assertThrows(ConcertNotOpenException.class,
                ()->concert.close());
    }
    @Test
    @DisplayName("예매 마감 공연은, 예매 마감에 실패한다.")
    void 공연예매마감_실패_CLOSED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();
        //when
        assertThrows(ConcertNotOpenException.class,
                ()->concert.close());
    }
    @Test
    @DisplayName("예매 취소 공연은, 예매 마감에 실패한다.")
    void 공연예매마감_실패_CANCELED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.cancel();
        //when
        assertThrows(ConcertNotOpenException.class,
                ()->concert.close());
    }
    @Test
    @DisplayName("종료된 공연은, 예매 마감에 실패한다.")
    void 공연예매마감_실패_FINISHED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();
        concert.finish();
        //when
        assertThrows(ConcertNotOpenException.class,
                ()->concert.close());
    }

    /**
     * 공연 종료
     */
    @Test
    @DisplayName("예매 마감 된 공연은, 공연 종료 할 수 있다.")
    void 공연종료_성공() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();

        //when
        concert.finish();

        //then
        Assertions.assertThat(concert.getStatus()).isEqualTo(ConcertStatus.FINISHED);
    }

    @Test
    @DisplayName("예매 준비 공연은, 공연 종료에 실패한다.")
    void 공연종료_실패_READY() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);

        //when
        assertThrows(ConcertNotCloseException.class,
                ()->concert.finish());
    }
    @Test
    @DisplayName("예매 오픈된 공연은, 공연 종료에 실패한다.")
    void 공연종료_실패_OPEN() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        //when
        assertThrows(ConcertNotCloseException.class,
                ()->concert.finish());
    }
    @Test
    @DisplayName("예매 취소 공연은, 공연 종료에 실패한다.")
    void 공연종료_실패_CANCELED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.cancel();
        //when
        assertThrows(ConcertNotCloseException.class,
                ()->concert.finish());
    }
    @Test
    @DisplayName("종료된 공연은, 공연 종료에 실패한다.")
    void 공연종료_실패_FINISHED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();
        concert.finish();
        //when
        assertThrows(ConcertNotCloseException.class,
                ()->concert.finish());
    }

    /**
     * 공연 취소
     */
    @Test
    @DisplayName("공연 취소에 성공하면, ConcertCanceledEvent 가 발행된다.")
    void 공연취소_성공_이벤트발행() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        //when
        concert.cancel();
        //then
        verify(publisher).publishEvent(any(ConcertCanceledEvent.class));
    }
    @Test
    @DisplayName("준비 상태의 공연은 공연을 취소할 수 있다.")
    void 공연취소_성공_READY() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        //when
        concert.cancel();
        //then
        Assertions.assertThat(concert.getStatus()).isEqualTo(ConcertStatus.CANCELED);
    }
    @Test
    @DisplayName("오픈 상태의 공연은 공연을 취소할 수 있다.")
    void 공연취소_성공_OPEN() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        //when
        concert.cancel();
        //then
        Assertions.assertThat(concert.getStatus()).isEqualTo(ConcertStatus.CANCELED);
    }
    @Test
    @DisplayName("예매 마감 상태의 공연은, 공연을 취소할 수 있다.")
    void 공연취소_성공_CLOSED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();
        //when
        concert.cancel();
        //then
        Assertions.assertThat(concert.getStatus()).isEqualTo(ConcertStatus.CANCELED);
    }
    @Test
    @DisplayName("예매 마감 상태의 공연은, 공연을 취소할 수 없다.")
    void 공연취소_실패_CANCELED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.cancel();
        //when
        assertThrows(ConcertNotActiveException.class,
                () -> concert.cancel());
    }
    @Test
    @DisplayName("종료 상태의 공연은, 공연을 취소할 수 없다.")
    void 공연취소_실패_FINISHED() {
        //given
        Concert concert = Concert.create("공연A", "공연A입니다.", 120, new Image(""), List.of(new GradePrice("VIP", new Money(100000))), 1L);
        concert.open();
        concert.close();
        concert.finish();
        //when
        assertThrows(ConcertNotActiveException.class,
                () -> concert.cancel());
    }
}
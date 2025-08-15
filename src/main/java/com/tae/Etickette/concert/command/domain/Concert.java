package com.tae.Etickette.concert.command.domain;

import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.exception.ConflictException;
import com.tae.Etickette.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String overview;
    @Column(nullable = false)
    private Integer runningTime;

    @Embedded
    private Image image;

    @Enumerated(value = EnumType.STRING)
    private ConcertStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "grade",
    joinColumns = @JoinColumn(name = "concert_number"))
    private List<GradePrice> gradePrices;

    /**
     * 연관관계
     */
    private Long venueId;

    public Concert(String title, String overview, Integer runningTime, Image image,  List<GradePrice> gradePrices,Long venueId) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.image = image;
        this.status = ConcertStatus.READY;
        this.gradePrices = gradePrices;
        //연관관계 설정
        this.venueId = venueId;
    }

    public static Concert create(String title, String overview, Integer runningTime, Image image, List<GradePrice> gradePrices,Long venueId) {
        return new Concert(title, overview, runningTime, image, gradePrices,venueId);
    }

    public void open() {
        verifyReady();
        this.status = ConcertStatus.OPEN;
    }
    //예매 완료
    public void close() {
        verifyOpened();
        this.status = ConcertStatus.SALE_CLOSED;
    }
    public void cancel() {
        verifyNotCancellable();
        this.status = ConcertStatus.CANCELED;
        //공연이 취소되면 스케줄 취소
        Events.raise(new ConcertCanceledEvent(this.getId()));
    }
    public void finish() {
        verifyClosed();
        this.status = ConcertStatus.FINISHED;
    }

    private void verifyReady() {
        if (this.status != ConcertStatus.READY) {
            throw new ConflictException(ErrorCode.CONCERT_NOT_READY, "공연이 준비되지 않았습니다.");
        }
    }
    private void verifyOpened() {
        if (this.status != ConcertStatus.OPEN) {
            throw new ConflictException(ErrorCode.CONCERT_NOT_OPEN, "오픈되지 않은 공연입니다.");
        }
    }

    private void verifyClosed() {
        if (this.status != ConcertStatus.SALE_CLOSED) {
            throw new ConflictException(ErrorCode.CONCERT_NOT_CLOSED, "공연 예매가 종료되지 않았습니다.");
        }
    }
    private void verifyNotCancellable() {
        if (!isCancellable()) {
            throw new ConflictException(ErrorCode.CONCERT_NOT_ACTIVATE, "취소할 수 없는 공연입니다.");
        }
    }
    private boolean isCancellable() {
        return this.status == ConcertStatus.READY || this.status == ConcertStatus.OPEN || this.status == ConcertStatus.SALE_CLOSED;
    }


}

package com.tae.Etickette.session.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column
    private LocalDate concertDate;
    @Column
    private LocalTime startTime;
    @Column
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
    /**
     * 연관관계
     */
    @Column
    private Long concertId;
    @Column
    private Long venueId;
    //예약 목록
    @ElementCollection
    @CollectionTable(name = "session_booking",joinColumns = @JoinColumn(name = "session_id"))
    private List<Booking> bookings = new ArrayList<>();
    //좌석 정보
    @ElementCollection
    @CollectionTable(name = "session_seat", joinColumns = @JoinColumn(name = "session_id"))
    private List<Seat> seatingPlan = new ArrayList<>();

    private Session(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId,Long venueId,List<Seat> seatingPlan) {
        this.concertDate = concertDate;
        this.startTime = startTime;
        calculateEndTime(startTime, runningTime);
        this.status = SessionStatus.BEFORE;
        this.concertId = concertId;
        this.venueId = venueId;
        this.seatingPlan = seatingPlan;
    }

    public static Session create(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId,Long venueId,
                                 List<Seat> seatingPlan) {
        return new Session(concertDate, startTime, runningTime, concertId, venueId, seatingPlan);
    }

    //공연 종료시간을 계산한다.
    private void calculateEndTime(LocalTime startTime, Integer runningTime) {
        this.endTime = startTime.plusMinutes(runningTime);
    }

    public void book(Long memberId, List<Seat> requestedSeats) {
        //좌석이 예매 가능한 지 검사한다.
        List<Seat> existSeats = verifySeatsExist(requestedSeats);
        //좌석이 유효한 지 검사한다.
        List<Seat> seats = verifySeatsAreFree(existSeats);

        Booking booking = new Booking(BookingRef.generate(), memberId, seats);
        bookings.add(booking);
    }
    private List<Seat> verifySeatsExist(List<Seat> requestedSeats) {
        if (!seatingPlan.containsAll(requestedSeats)) {
            throw new RuntimeException();
        }
        return requestedSeats;
    }
    private List<Seat> verifySeatsAreFree(List<Seat> requestedSeats) {
        List<Seat> alreadyBooked = bookings.stream()
                .flatMap(b -> b.getSeats().stream())
                .filter(requestedSeats::contains)
                .toList();
        if (!alreadyBooked.isEmpty()) {
            throw new RuntimeException();
        }
        return requestedSeats;
    }

    public boolean isActive() {
        return this.status == SessionStatus.BEFORE || this.status == SessionStatus.OPEN;
    }

    public void openSchedule() {
        this.status = SessionStatus.OPEN;
    }
    public void cancel() {
        this.status = SessionStatus.CANCELED;
    }


}

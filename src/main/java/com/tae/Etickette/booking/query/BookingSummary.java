package com.tae.Etickette.booking.query;

import com.tae.Etickette.booking.command.domain.BookingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 예매 요약 내역 조회 전용 엔티티
 * 예매 요약 내역(티켓)은 자주 조회되고,
 * 페이징/정렬을 사용하므로 Spring Data JPA 기능을 사용하기로 결정
*/
@Entity
@Immutable
@Synchronize({"booking","session","concert","venue"})
@Subselect("""
select
    b.booking_ref, c.title as concert_name, c.image_path,
    b.status ,s.concert_date, s.start_time as concert_time,
    v.place as venue_name, b.member_id,
    (select count(li.seat_id) from line_item li where li.booking_id = b.booking_ref )as size
    from booking b
    join session s on b.session_id = s.session_id
    join concert c on s.concert_id = c.concert_id
    join venue v on c.venue_id = v.venue_id
""")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingSummary implements Serializable {
    //예약 번호
    @Id
    @Column(name = "booking_ref")
    private String bookingRef;
    @Column(name = "concert_name")
    private String concertName;
    private String imagePath;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @Column(name = "concert_date")
    private LocalDate concertDate;
    @Column(name = "concert_time")
    private LocalTime concertTime;
    @Column(name = "venue_name")
    private String venueName;
    @Column(name = "member_id")
    private Long memberId;
    //매수
    private int size;
}

package com.tae.Etickette.bookseat.query;

import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.command.domain.SeatStatus;
import com.tae.Etickette.global.jpa.MoneyConverter;
import com.tae.Etickette.global.model.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(BookSeatId.class)
@Table(name = "book_seat",indexes = @Index(name = "idx_book_seat",columnList = "session_id"))
public class BookSeatData {
    @Id
    @Column(name = "seat_id")
    private Long seatId;
    @Id
    @Column(name = "session_id")
    private Long sessionId;

    private String grade;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;
}

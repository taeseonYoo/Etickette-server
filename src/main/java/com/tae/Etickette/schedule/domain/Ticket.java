package com.tae.Etickette.schedule.domain;

import com.tae.Etickette.booking.Money;
import com.tae.Etickette.booking.MoneyConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private Long scheduleId;
    private Long seatId;
//    @Convert(converter = MoneyConverter.class)
//    private Money price;
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;

    public Ticket(Long scheduleId, Long seatId) {
        this.scheduleId = scheduleId;
        this.seatId = seatId;
    }

    public void book() {
        this.status = TicketStatus.RESERVED;
    }

    public void cancel() {
        this.status = TicketStatus.AVAILABLE;
    }
}

package com.tae.Etickette.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BookingRequestDto {

    private Long memberId;
    private Long scheduleId;
    //TODO 결제정보가 들어가야하는가?
    private List<SeatInfo> seats;

    @Builder
    public BookingRequestDto(Long memberId, Long scheduleId, List<SeatInfo> seats) {
        this.memberId = memberId;
        this.scheduleId = scheduleId;
        this.seats = seats;
    }

    @Getter
    @AllArgsConstructor
    public static class SeatInfo {
        private String grade;
        private String row;
        private Integer column;

        public Seat toSeat() {
            return new Seat(this.row, this.column);
        }
    }
}

package com.tae.Etickette.booking.query.application;

import com.tae.Etickette.booking.command.domain.SeatItem;
import com.tae.Etickette.seat.query.SeatData;
import lombok.Getter;

@Getter
public class LineItemInfo {

    private int price;
    private String seatInfo;

    public LineItemInfo(SeatItem seatItem, SeatData seatData) {
        this.price = seatItem.getPrice().getValue();
        this.seatInfo = seatData.getRow() + seatData.getColumn();
    }
}

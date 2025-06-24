package com.tae.Etickette.booking.query;

import com.tae.Etickette.booking.domain.LineItem;
import com.tae.Etickette.seat.query.SeatData;
import lombok.Getter;

@Getter
public class LineItemInfo {

    private int price;
    private String seatInfo;

    public LineItemInfo(LineItem lineItem, SeatData seatData) {
        this.price = lineItem.getPrice().getValue();
        this.seatInfo = seatData.getRow() + seatData.getColumn();
    }
}

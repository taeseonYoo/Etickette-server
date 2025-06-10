package com.tae.Etickette.payment;

import lombok.Getter;

@Getter
public class SaveAmountRequestDto {
    String orderId;
    String totalAmount;
    String paymentKey;

    public SaveAmountRequestDto(String orderId, String totalAmount, String paymentKey) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.paymentKey = paymentKey;
    }
}

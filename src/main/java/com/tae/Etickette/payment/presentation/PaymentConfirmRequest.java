package com.tae.Etickette.payment.presentation;

import lombok.Getter;

@Getter
public class PaymentConfirmRequest {
    String paymentKey;
    String orderId;
    String amount;

    public PaymentConfirmRequest(String paymentKey, String orderId, String amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}

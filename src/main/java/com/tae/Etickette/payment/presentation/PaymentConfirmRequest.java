package com.tae.Etickette.payment.presentation;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PaymentConfirmRequest {
    @NotBlank
    String paymentKey;
    @NotBlank
    String orderId;
    @NotBlank
    String amount;

    public PaymentConfirmRequest(String paymentKey, String orderId, String amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}

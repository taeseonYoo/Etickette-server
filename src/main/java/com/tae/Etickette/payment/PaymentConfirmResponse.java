package com.tae.Etickette.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentConfirmResponse {
    private String orderId;
    private String paymentKey;
    private String amount;
    private String status;
}

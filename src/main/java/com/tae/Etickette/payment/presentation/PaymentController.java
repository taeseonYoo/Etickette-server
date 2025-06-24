package com.tae.Etickette.payment.presentation;

import com.tae.Etickette.payment.application.TossPaymentService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final TossPaymentService tossPaymentService;

    @PostMapping("/confirm")
    public void confirmPayment(@RequestBody PaymentConfirmRequest request) {

        try {
            JSONObject json = new JSONObject();
            json.put("paymentKey", request.getPaymentKey());
            json.put("orderId", request.getOrderId());
            json.put("amount", request.getAmount());

            tossPaymentService.confirm(json);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

}

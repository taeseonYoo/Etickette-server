package com.tae.Etickette.payment;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.http.ResponseEntity;
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



    @PostMapping("/saveAmount")
    public void tempSave(@RequestBody SaveAmountRequestDto requestDto) {

    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentConfirmResponse> confirmPayment(@RequestBody PaymentConfirmRequest paymentConfirmRequest) throws IOException, ParseException {
        System.out.println("gd");
        PaymentConfirmResponse response = tossPaymentService.confirm(paymentConfirmRequest);
        return ResponseEntity.ok().body(response);
    }

}

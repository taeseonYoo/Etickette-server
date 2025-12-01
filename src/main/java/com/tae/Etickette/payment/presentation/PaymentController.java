package com.tae.Etickette.payment.presentation;

import com.tae.Etickette.global.api.SuccessResponse;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ServerProcessException;
import com.tae.Etickette.payment.application.TossPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Payment API",description = "결제 관련 API")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final TossPaymentService tossPaymentService;

    @Operation(summary = "결제 승인 요청",description = "결제 key, 주문 번호, 주문 가격을 확인한다.")
    @PostMapping("/confirm")
    public ResponseEntity<SuccessResponse<Void>> confirmPayment(@Valid @RequestBody PaymentConfirmRequest request) {

        try {
            JSONObject json = new JSONObject();
            json.put("paymentKey", request.getPaymentKey());
            json.put("orderId", request.getOrderId());
            json.put("amount", request.getAmount());

            tossPaymentService.confirm(json);
        } catch (IOException | ParseException e) {
            throw new ServerProcessException(ErrorCode.PAYMETHOD_NOT_SUPPOERTED,"결제 승인에 실패했습니다.");
        }
        return ResponseEntity.noContent().build();
    }

}

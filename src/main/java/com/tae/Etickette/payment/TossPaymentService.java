package com.tae.Etickette.payment;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TossPaymentService {

    @Value(("${toss.secret-key}"))
    private String secretKey;
    @Value(("${toss.base-url}"))
    private String baseUrl;

    @Transactional
    public PaymentConfirmResponse confirm(PaymentConfirmRequest request) throws IOException, ParseException {

        String widgetSecretKey = secretKey;
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        JSONObject json = new JSONObject();
        json.put("paymentKey",request.getPaymentKey());
        json.put("orderId", request.getOrderId());
        json.put("amount", request.getAmount());

        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        URL url = new URL(baseUrl + "/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(json.toString().getBytes(StandardCharsets.UTF_8));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(new InputStreamReader(responseStream, StandardCharsets.UTF_8));

        // JSON -> DTO 수동 매핑
        PaymentConfirmResponse paymentResponse = new PaymentConfirmResponse();
        paymentResponse.setOrderId((String) responseJson.get("orderId"));
        paymentResponse.setPaymentKey((String) responseJson.get("paymentKey"));
        paymentResponse.setAmount((String) responseJson.get("amount"));
        paymentResponse.setStatus((String) responseJson.get("status"));

        return paymentResponse;
    }
}

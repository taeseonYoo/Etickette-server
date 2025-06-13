package com.tae.Etickette.payment.application;

import com.tae.Etickette.payment.domain.TossPaymentsVariables;
import com.tae.Etickette.payment.domain.PayMethod;
import com.tae.Etickette.payment.domain.Payment;
import com.tae.Etickette.payment.infra.PaymentRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final PaymentRepository paymentRepository;

    @Value(("${toss.secret-key}"))
    private String secretKey;
    @Value(("${toss.base-url}"))
    private String baseUrl;

    JSONParser parser = new JSONParser();
    @Transactional
    public void confirm(JSONObject tossRequest) throws ParseException, IOException {
        HttpURLConnection connection = getTossResult(tossRequest);


        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;
        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
        JSONObject responseJson = (JSONObject) parser.parse(new InputStreamReader(responseStream, StandardCharsets.UTF_8));

        if (!isSuccess) {
            fail(responseJson);
        }
        success(responseJson);

    }

    private void success(JSONObject tossResult) {
        Payment payment = new Payment(
                (String) tossResult.get(TossPaymentsVariables.VERSION.getValue()),
                (String) tossResult.get(TossPaymentsVariables.PAYMENTKEY.getValue()),
                (String) tossResult.get(TossPaymentsVariables.ORDERID.getValue()),
                (String) tossResult.get(TossPaymentsVariables.ORDERNAME.getValue()),
                (String) tossResult.get(TossPaymentsVariables.MID.getValue()),
                (String) tossResult.get(TossPaymentsVariables.STATUS.getValue()),
                ((Integer) tossResult.get(TossPaymentsVariables.TOTALAMOUNT.getValue())),
                ((Integer) tossResult.get(TossPaymentsVariables.BALANCEAMOUNT.getValue())),
                PayMethod.from(((String) tossResult.get(TossPaymentsVariables.METHOD.getValue()))),
                (String) tossResult.get(TossPaymentsVariables.REQUESTEDAT.getValue()),
                (String) tossResult.get(TossPaymentsVariables.APPROVEDAT.getValue()));
        paymentRepository.save(payment);
    }

    private void fail(JSONObject tossResult) {
        //TODO 결제에 실패하는 경우..
    }
    private HttpURLConnection getTossResult(JSONObject tossRequest) throws IOException {
        String widgetSecretKey = secretKey;
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        URL url = new URL(baseUrl + "/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(tossRequest.toString().getBytes(StandardCharsets.UTF_8));
        return connection;
    }
}

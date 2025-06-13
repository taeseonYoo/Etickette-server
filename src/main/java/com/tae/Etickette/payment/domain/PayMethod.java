package com.tae.Etickette.payment.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PayMethod {
    EASY_PAY("간편결제"),
    MOBILE_PHONE("휴대폰"),
    TRANSFER("계좌이체"),
    CARD("카드"),
    CULTURE_GIFT_CERTIFICATE("문화상품권"),
    BOOK_GIFT_CERTIFICATE("도서문화상품권"),
    GAME_GIFT_CERTIFICATE("게임문화상품권"),
    VIRTUAL_ACCOUNT("가상계좌");
    private String value;
    PayMethod(String value) {
        this.value = value;
    }
    public static PayMethod from(String value) {
        return Arrays.stream(values())
                .filter(pm -> pm.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown PayMethod: " + value));
    }
}

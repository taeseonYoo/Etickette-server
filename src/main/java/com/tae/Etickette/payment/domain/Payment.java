package com.tae.Etickette.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String version; // 객체의 응답 버전

    @Column(nullable = false)
    private String paymentKey; // 결제의 키값, 결제를 식별하는 역할

    @Column(nullable = false)
    private String orderId; // 주문 ID , 여기서는 Booking id

    @Column
    private String orderName; // 구매상품, 예를들어 "생수 외 1건"

    @Column(nullable = false)
    private String mid; // 가맹점 ID

    @Column
    private String status; // 결제 상태 (READY,IN_PROGRESS,WAITING_FOR_DEPOSIT,DONE,CANCELED,PARTIAL_CANCELED,ABORTED,EXPIRED)

    @Column
    private Integer totalAmount; // 총 결제 금액

    @Column
    private Integer balanceAmount; // 남은 결제 금액

    @Enumerated(EnumType.STRING)
    @Column
    private PayMethod method; // 결제 수단 (카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권)

    @Column
    private String requestedAt; // 결제 요청 시간

    @Column
    private String approvedAt; // 결제 승인 시간
    @Builder
    public Payment(String version, String paymentKey, String orderId, String orderName, String mid, String status, Integer totalAmount, Integer balanceAmount, PayMethod method, String requestedAt, String approvedAt) {
        this.version = version;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.mid = mid;
        this.status = status;
        this.totalAmount = totalAmount;
        this.balanceAmount = balanceAmount;
        this.method = method;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
    }
}


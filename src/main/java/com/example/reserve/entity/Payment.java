package com.example.reserve.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private Long amount; //결제 금액

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String paymentKey; //외부 결제 식별자 (실제 연동 시 필요)

    private LocalDateTime approvedAt; //결제 승인 시간

    public static Payment createPayment(Reservation reservation, Long amount) {
        Payment payment = new Payment();
        payment.reservation = reservation;
        payment.amount = amount;
        payment.status = PaymentStatus.READY;
        return payment;
    }

    public void complete(String paymentKey) {
        this.status = PaymentStatus.COMPLETED;
        this.paymentKey = paymentKey;
        this.approvedAt = LocalDateTime.now();
    }


}

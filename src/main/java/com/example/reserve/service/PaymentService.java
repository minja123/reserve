package com.example.reserve.service;

import com.example.reserve.entity.Payment;
import com.example.reserve.entity.PaymentStatus;
import com.example.reserve.entity.Reservation;
import com.example.reserve.exception.ReservationException;
import com.example.reserve.repository.PaymentRepository;
import com.example.reserve.repository.ReservationRepository;
import com.example.reserve.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    public void processPayment(Long reservationId, String decision) {
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationException("결제 정보를 찾을 수 없습니다."));

        Reservation reservation = payment.getReservation();

        if ("SUCCESS".equals(decision)) {
            //1. 결제 완료 처리
            payment.complete("MOCK_KEY_" + UUID.randomUUID());

            //2. 예약 확정
            reservation.confirm();

            //3. 타임슬롯 확정(벌크 연산 사용 가능)
            timeSlotRepository.reserve(reservation.getTimeSlotId());
        } else {
            //실패 시 처리
            payment.setStatus(PaymentStatus.CANCELLED);
            reservation.cancel();

            //타임슬롯 다시 열어주기
            timeSlotRepository.release(reservation.getTimeSlotId());
        }
    }
}

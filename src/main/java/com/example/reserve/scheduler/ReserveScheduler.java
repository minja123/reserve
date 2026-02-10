package com.example.reserve.scheduler;

import com.example.reserve.entity.Reservation;
import com.example.reserve.repository.PaymentRepository;
import com.example.reserve.repository.ReservationRepository;
import com.example.reserve.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReserveScheduler {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    // 1. 5분마다 실행하여, 5분이 지난 예약을 자동 취소 (이전 시작 시점 기준)
    @Scheduled(fixedDelay = 300000)
    public void runReservationCancel() {
        System.out.println("60초 주기 실행: " + LocalDateTime.now());

        LocalDateTime threshold = LocalDateTime.now().minusSeconds(300);

        System.out.println(threshold);

        List<Reservation> expiredReservations = reservationRepository.findExpiredReservations(threshold);

        for (Reservation expiredReservation : expiredReservations) {
            reservationService.expireReservation(expiredReservation);
        }

    }
}

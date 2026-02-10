package com.example.reserve.service;

import com.example.reserve.dto.ReservationRequest;
import com.example.reserve.entity.*;
import com.example.reserve.exception.ReservationException;
import com.example.reserve.repository.PaymentRepository;
import com.example.reserve.repository.ReservationRepository;
import com.example.reserve.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final PaymentRepository paymentRepository;


    @Transactional
    public Long create(ReservationRequest request) {

        //1.조회 시점에 락을 걸어버림
        TimeSlot slot = timeSlotRepository.findByIdWithLock(request.getTimeSlotId())
                .orElseThrow(() -> new ReservationException("존재하지 않습니다."));

        //2.상태 체크 (날짜가 이미 지난 슬롯인지 체크하는 로직 추가
        if (slot.getReservationDate().isBefore(LocalDate.now())) {
            throw new ReservationException("지난 날짜는 예약할 수 없습니다.");
        }

        if (!slot.isAvailable()) {
            throw new ReservationException("이미 예약 중이거나 완료된 시간대입니다.");
        }

        // 3. 상태 변경 (영속성 컨텍스트 덕분에 따로 update 안 해도 됨)
        slot.hold();

        // 예약 및 결제 생성
        // 만약 unique 제약 조건 등으로 에러가 나면
        // DataIntegrityViolationException이 발생하고, 어드바이스가 가로챕니다.
        Reservation reservation = Reservation.create(request.getUserEmail(), request.getTimeSlotId());
        Reservation saved = reservationRepository.save(reservation);

        Payment payment = Payment.createPayment(reservation, 15000L);
        paymentRepository.save(payment);

//            timeSlotRepository.reserve(request.getTimeSlotId());

        return saved.getId();


    }

    @Transactional
    public void cancelReservation(Reservation reservation) {
        Long resId = reservation.getId();
        Long timeSlotId = reservation.getTimeSlotId();

        Optional<Payment> byReservationId = paymentRepository.findByReservationId(resId);


        if(byReservationId.isPresent()) {
            //결제 삭제
            paymentRepository.delete(byReservationId.get());

            //예약 삭제
            reservationRepository.delete(reservation);

            //시간을 HOLD -> AVAILABLE로 업데이트
            timeSlotRepository.available(timeSlotId);
        }
    }

    // 스케줄러에서 호풀할 "미결제 예약 취소" 로직
    @Transactional
    public void expireReservation(Reservation reservation) {
        //1. 결제 정보 취소 상태로 변경(삭제 대신 기록 유지 권장)
        paymentRepository.findByReservationId(reservation.getId())
                .ifPresent(payment -> payment.setStatus(PaymentStatus.EXPIRED));

        //2. 예약 상태 취소로 변경
        reservation.cancel();

        //3. 타임슬롯 다시 복수
        TimeSlot slot = timeSlotRepository.findById(reservation.getTimeSlotId())
                .orElseThrow();

        slot.available();

    }
}

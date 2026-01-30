package com.example.reserve.service;

import com.example.reserve.dto.ReservationRequest;
import com.example.reserve.entity.Payment;
import com.example.reserve.entity.Reservation;
import com.example.reserve.entity.TimeSlot;
import com.example.reserve.entity.TimeSlotStatus;
import com.example.reserve.exception.ReservationException;
import com.example.reserve.repository.PaymentRepository;
import com.example.reserve.repository.ReservationRepository;
import com.example.reserve.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final PaymentRepository paymentRepository;


    @Transactional
    public Long create(ReservationRequest request) {

//        int updated = timeSlotRepository.holdIfAvailable(request.getTimeSlotId());
//
//        if(updated == 0) {
//            throw new ReservationException("이미 예약 중이거나 존재하지 않는 시간입니다.");
//        }

        //1.조회 시점에 락을 걸어버림
        TimeSlot slot = timeSlotRepository.findByIdWithLock(request.getTimeSlotId())
                .orElseThrow(() -> new ReservationException("존재하지 않습니다."));

        //2.상태 체크
        if (slot.getStatus() != TimeSlotStatus.AVAILABLE) {
            throw new ReservationException("이미 예약 중입니다.");
        }

        slot.setStatus(TimeSlotStatus.HOLD);

        try {
            Reservation reservation = Reservation.create(request.getUserEmail(), request.getTimeSlotId());

            //예약 저장
            Reservation saved = reservationRepository.save(reservation);

            //예약에 대한 결제 생성
            Payment payment = Payment.createPayment(reservation, 15000L);

            paymentRepository.save(payment);

//            timeSlotRepository.reserve(request.getTimeSlotId());

            return saved.getId();

        } catch (DataIntegrityViolationException e) {
            throw new ReservationException("이미 예약된 시간입니다.");
        }
    }
}

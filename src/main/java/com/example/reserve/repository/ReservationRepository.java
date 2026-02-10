package com.example.reserve.repository;

import com.example.reserve.entity.Reservation;
import com.example.reserve.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByTimeSlotId(Long timeSlotId);

    // JPQL 사용 시
    @Query("""
            SELECT r
              FROM Reservation r
             WHERE r.status = 'PENDING'
               AND r.createdAt < :threshold
            """)
    List<Reservation> findExpiredReservations(@Param("threshold")LocalDateTime threshold);

    // 쿼리 메서드 방식 (메서드 이름만으로 쿼리 생성 - 더 간단함!)
    List<Reservation> findByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime threshold);
}

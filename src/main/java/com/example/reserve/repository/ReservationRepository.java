package com.example.reserve.repository;

import com.example.reserve.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByTimeSlotId(Long timeSlotId);
}

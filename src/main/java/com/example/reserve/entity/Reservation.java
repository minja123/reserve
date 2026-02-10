package com.example.reserve.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reservation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reservation_time_slot",
                        columnNames = "time_slot_id"
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@ToString
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long timeSlotId;

    @Column(length = 100)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Reservation create(String userEmail, Long timeSlotId) {
        Reservation r = new Reservation();
        r.timeSlotId = timeSlotId;
        r.userEmail = userEmail;
        r.status = ReservationStatus.PENDING;
        return r;
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
}

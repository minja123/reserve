package com.example.reserve.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(name = "timeSlot")
@Entity
@Setter @Getter
public class TimeSlot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private LocalDate reservationDate; // 날짜 필드 추가 (2024-05-20 등)
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime; // 종료 시간

    @Enumerated(EnumType.STRING)
    private TimeSlotStatus status;

    public boolean isAvailable() {
        return this.status == TimeSlotStatus.AVAILABLE;
    }

    public void available() { this.status = TimeSlotStatus.AVAILABLE; }

    public void hold() {
        this.status = TimeSlotStatus.HOLD;
    }

    public void reserve() {
        this.status = TimeSlotStatus.RESERVED;
    }
}

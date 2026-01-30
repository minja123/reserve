package com.example.reserve.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "timeSlot")
@Entity
@Setter @Getter
public class TimeSlot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TimeSlotStatus status;

    public boolean isAvailable() {
        return this.status == TimeSlotStatus.AVAILABLE;
    }

    public void hold() {
        this.status = TimeSlotStatus.HOLD;
    }

    public void reserve() {
        this.status = TimeSlotStatus.RESERVED;
    }
}

package com.example.reserve.dto;

import com.example.reserve.entity.TimeSlot;
import com.example.reserve.entity.TimeSlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TimeslotResponse {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeSlotStatus status;

    public static TimeslotResponse from(TimeSlot timeSlot) {
        return new TimeslotResponse(
                timeSlot.getId(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                timeSlot.getStatus()
        );
    }
}

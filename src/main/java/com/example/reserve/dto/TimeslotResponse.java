package com.example.reserve.dto;

import com.example.reserve.entity.TimeSlot;
import com.example.reserve.entity.TimeSlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeslotResponse {
    private Long id;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private TimeSlotStatus status;

    public static TimeslotResponse from(TimeSlot timeSlot) {
        return new TimeslotResponse(
                timeSlot.getId(),
                timeSlot.getReservationDate(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                timeSlot.getStatus()
        );
    }
}

package com.example.reserve.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private String userEmail;
    private Long TimeSlotId;

    public static ReservationRequest create(String userEmail, Long timeSlotId) {
        ReservationRequest request = new ReservationRequest();
        request.setUserEmail(userEmail);
        request.setTimeSlotId(timeSlotId);
        return request;
    }
}

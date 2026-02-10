package com.example.reserve.controller;

import com.example.reserve.dto.ApiResponse;
import com.example.reserve.dto.ReservationRequest;
import com.example.reserve.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ApiResponse<?> createReservation(@RequestBody ReservationRequest request) {
        Long reservationId = reservationService.create(request);
        return ApiResponse.success(reservationId);
    }
}

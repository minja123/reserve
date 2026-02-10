package com.example.reserve.controller;

import com.example.reserve.dto.ApiResponse;
import com.example.reserve.dto.TimeslotResponse;
import com.example.reserve.service.TimeslotService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeslotService timeslotService;

    @GetMapping
    public ApiResponse<List<TimeslotResponse>> findByResourceId(
            @RequestParam(name = "resourceId") Long id,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
            ){
        List<TimeslotResponse> timeslotResponseList = timeslotService.findByResourceId(id, date);
        return ApiResponse.success(timeslotResponseList);
    }

}

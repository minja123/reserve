package com.example.reserve.controller;

import com.example.reserve.dto.ApiResponse;
import com.example.reserve.dto.TimeslotResponse;
import com.example.reserve.service.TimeslotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeslotService timeslotService;

    @GetMapping
    public ApiResponse<List<TimeslotResponse>> findByResourceId(@RequestParam(name = "resourceId") Long id){
        List<TimeslotResponse> timeslotResponseList = timeslotService.findByResourceId(id);
        return ApiResponse.success(timeslotResponseList);
    }

}

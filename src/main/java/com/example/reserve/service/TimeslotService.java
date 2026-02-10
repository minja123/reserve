package com.example.reserve.service;

import com.example.reserve.dto.TimeslotResponse;
import com.example.reserve.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeslotService {

    private final TimeSlotRepository timeSlotRepository;

    public List<TimeslotResponse> findByResourceId(Long id, LocalDate date) {
        return timeSlotRepository.findByResourceIdAndReservationDate(id, date).stream()
                .map(TimeslotResponse::from)
                .toList();
    }
}

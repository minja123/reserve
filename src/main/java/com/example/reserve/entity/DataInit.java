package com.example.reserve.entity;

import com.example.reserve.repository.ResourceRepository;
import com.example.reserve.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final ResourceRepository resourceRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if(resourceRepository.count() > 0) {
            return;
        }

        //1. Resource 생성
        Resource roomA = new Resource();
        roomA.setName("회의실 A");
        roomA.setCategory("ROOM");
        resourceRepository.save(roomA);

        // 2.해당 Resource에 대한 TimeSlot 생성 (반복문으로 여러 개 생성 가능)
        for (int i = 9; i < 18; i++) {
            TimeSlot slot = new TimeSlot();
            slot.setResource(roomA);
            slot.setStartTime(LocalDateTime.of(2024, 5, 20, i, 0));
            slot.setEndTime(LocalDateTime.of(2024, 5, 20, i + 1, 0));
            slot.setStatus(TimeSlotStatus.AVAILABLE);
            timeSlotRepository.save(slot);
        }
    }
}

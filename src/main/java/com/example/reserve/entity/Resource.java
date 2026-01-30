package com.example.reserve.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Resource {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 예: 회의실 A, 테니스 코트 1

    private String description;

    @Column(nullable = false)
    private String category; // ROOM, EQUIPMENT, SPORTS
}

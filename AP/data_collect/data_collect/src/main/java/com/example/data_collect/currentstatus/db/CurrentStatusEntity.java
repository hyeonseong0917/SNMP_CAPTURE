package com.example.data_collect.currentstatus.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "current_ip_status")
public class CurrentStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id
    private String ipAddress; // ip주소
    private Integer connectionStatus; // 연결 상태
    private LocalDateTime arrivalTime; // 큐 도착 시간
}


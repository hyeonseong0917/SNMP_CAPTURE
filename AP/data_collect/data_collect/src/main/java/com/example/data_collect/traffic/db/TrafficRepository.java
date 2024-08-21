package com.example.data_collect.traffic.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrafficRepository extends JpaRepository<TrafficEntity, Long> {
    // arrival_time 기준으로 가장 최근 항목 하나만 가져오기
    Optional<TrafficEntity> findFirstByOrderByArrivalTimeDesc();
}

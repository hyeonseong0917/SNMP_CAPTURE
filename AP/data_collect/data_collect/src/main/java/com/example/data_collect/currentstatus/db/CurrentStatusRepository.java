package com.example.data_collect.currentstatus.db;

import com.example.data_collect.status.db.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrentStatusRepository extends JpaRepository<CurrentStatusEntity,Long> {
    Optional<CurrentStatusEntity> findByIpAddress(String ipAddress);
}

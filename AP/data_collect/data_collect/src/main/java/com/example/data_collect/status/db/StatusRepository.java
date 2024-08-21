package com.example.data_collect.status.db;

import com.example.data_collect.interfaceIndex.db.InterfaceIndexEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<StatusEntity,String> {
    Optional<StatusEntity> findTopByIpAddress(String ipAddress);
}

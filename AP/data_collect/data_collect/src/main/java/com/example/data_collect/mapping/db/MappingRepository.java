package com.example.data_collect.mapping.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingRepository extends JpaRepository<MappingEntity, MappingEntityId> {
}

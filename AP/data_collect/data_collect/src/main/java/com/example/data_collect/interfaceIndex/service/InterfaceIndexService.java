package com.example.data_collect.interfaceIndex.service;

import com.example.data_collect.common.FieldKey;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexEntity;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexId;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexRepository;
//import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class InterfaceIndexService {
    @Autowired
    private InterfaceIndexRepository interfaceIndexRepository;

    public InterfaceIndexEntity getInterfaceIndexEntity(Map<String, Integer> fields, Map<String, String> tags, LocalDateTime localDateTime) {
        return InterfaceIndexEntity.builder()
                .ifIndex(fields.get(FieldKey.IF_INDEX.getKey()))
                .arrivalTime(localDateTime)
                .ifName(tags.get(FieldKey.IF_DESCR.getKey()))
                .build();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveOrUpdate(InterfaceIndexEntity interfaceIndexEntity) {
        Optional<InterfaceIndexEntity> existingEntity = interfaceIndexRepository.findTopByIfName(interfaceIndexEntity.getIfName());
        if (existingEntity.isPresent()) {
            InterfaceIndexEntity entity=existingEntity.get();
            entity.setIfIndex(interfaceIndexEntity.getIfIndex());
            entity.setArrivalTime(interfaceIndexEntity.getArrivalTime());
            interfaceIndexRepository.save(entity);
        } else {
            interfaceIndexRepository.save(interfaceIndexEntity);
        }
    }
    //0814

}

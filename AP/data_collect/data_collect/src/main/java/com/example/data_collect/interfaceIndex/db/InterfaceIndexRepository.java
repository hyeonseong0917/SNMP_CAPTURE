package com.example.data_collect.interfaceIndex.db;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

//public interface InterfaceIndexRepository extends JpaRepository<InterfaceIndexEntity,Long> {
////    InterfaceIndexEntity findByIfName(String ifName);
//    InterfaceIndexEntity findTopByIfName(String ifName);
////    InterfaceIndexEntity findTopByIfIndex(Integer ifIndex);
//    Optional<InterfaceIndexEntity> findTopByIfIndex(Integer ifIndex);
////    Optional<InterfaceIndexEntity> findTopByIfIndex(Object ifIndex);
//
//}
public interface InterfaceIndexRepository extends JpaRepository<InterfaceIndexEntity, String> {
    Optional<InterfaceIndexEntity> findTopByIfIndex(Integer ifIndex);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<InterfaceIndexEntity> findTopByIfName(String ifName);
}
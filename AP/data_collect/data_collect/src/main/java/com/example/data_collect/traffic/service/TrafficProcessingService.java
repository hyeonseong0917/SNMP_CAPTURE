package com.example.data_collect.traffic.service;

import com.example.data_collect.common.FieldKey;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexEntity;
import com.example.data_collect.interfaceIndex.service.InterfaceIndexService;
import com.example.data_collect.traffic.db.TrafficEntity;
import com.example.data_collect.traffic.db.TrafficRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@Service
public class TrafficProcessingService {
    @Autowired
    private TrafficRepository trafficRepository;


    @Transactional
    public void processTrafficInfo(Map<String, Integer> fields, Map<String, String> tags, Integer timestampFields) {
        LocalDateTime localDateTime = getLocalDateTime(timestampFields);
        try {
            TrafficEntity trafficEntity = getTrafficEntity(fields, tags, localDateTime); // GET traffic Entity
            trafficRepository.save(trafficEntity); // DB에 Entity 저장
        } catch (Exception e) {
            System.err.println("Error Occured in ProcessTrafficInfo");
            e.printStackTrace();
        }
    }

    private LocalDateTime getLocalDateTime(Integer timestampFields) {
        Instant instant = Instant.ofEpochSecond(timestampFields);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private TrafficEntity getTrafficEntity(Map<String, Integer> fields, Map<String, String> tags, LocalDateTime localDateTime) {
        return TrafficEntity.builder()
                .arrivalTime(localDateTime) // 시간 저장
                .adminStatus(getLongValue(fields.get(FieldKey.IF_ADMIN_STATUS.getKey())))
                .operStatus(getLongValue(fields.get(FieldKey.IF_OPER_STATUS.getKey())))
                .inErrorPacket(getLongValue(fields.get(FieldKey.IF_IN_ERROR_PACKET.getKey())))
                .outErrorPacket(getLongValue(fields.get(FieldKey.IF_OUT_ERROR_PACKET.getKey())))
                .inPacket(getLongValue(fields.get(FieldKey.IF_IN_OCTETS.getKey())))
                .outPacket(getLongValue(fields.get(FieldKey.IF_OUT_OCTETS.getKey())))
                .ifName(tags.get(FieldKey.IF_DESCR.getKey()))
                .build();
    }
    private Long getLongValue(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else {
            throw new IllegalArgumentException("Unexpected type: " + obj.getClass().getName());
        }
    }
}

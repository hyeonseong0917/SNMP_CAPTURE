package com.example.data_collect.interfaceIndex.service;

import com.example.data_collect.interfaceIndex.db.InterfaceIndexEntity;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
public class InterfaceIndexProcessingService {
    @Autowired
    private InterfaceIndexService interfaceIndexService;




//    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Transactional
    public void processInterfaceIndexInfo(Map<String, Integer> fields, Map<String, String> tags, Integer timestampFields){
        LocalDateTime localDateTime = getLocalDateTime(timestampFields);
        try {
            InterfaceIndexEntity interfaceIndexEntity = interfaceIndexService.getInterfaceIndexEntity(fields, tags, localDateTime);
            interfaceIndexService.saveOrUpdate(interfaceIndexEntity);
        } catch (Exception e) {
            System.err.println("Error Occured in interfaceIndexProcessingService processInterfaceIndexInfo");
            e.printStackTrace();
        }
    }

    private LocalDateTime getLocalDateTime(Integer timestampFields) {
        Instant instant = Instant.ofEpochSecond(timestampFields);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}

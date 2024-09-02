package com.example.data_collect.mapping.service;

import com.example.data_collect.common.FieldKey;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexEntity;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexRepository;
import com.example.data_collect.status.service.StatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MappingMessageService {
    @Autowired
    private InterfaceIndexRepository interfaceIndexRepository;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private StatusService statusService;

    @Transactional
    public void processMessage(String message) {
        List<InterfaceIndexEntity> interfaceIndexList = interfaceIndexRepository.findAll();
        int retry=3;
        while(interfaceIndexList.isEmpty() && retry>0){
            try {
                Thread.sleep(1000);
                interfaceIndexList = interfaceIndexRepository.findAll();
            }catch(Exception e){
                System.out.println("Thread Error occur");
            }
            --retry;
        }
        if (!interfaceIndexList.isEmpty()) {
            // if_index 테이블이 비어있지 않을 때
            List<String> lines = Arrays.asList(message.split("\n")); // 메시지 각 줄
            lines.forEach(line -> processActiveIpLine(line)); // 각 줄에 대해 메서드 호출
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, Object> map = objectMapper.readValue(lines.get(0), Map.class); // 역직렬화
                Integer timestampFields = (Integer) map.get("timestamp"); // 시간
                LocalDateTime localDateTime = getLocalDateTime(timestampFields);
                statusService.updateStatus(localDateTime);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Transactional
    private void processActiveIpLine(String line) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(line, Map.class); // 역직렬화
            Map<String, Object> fields = (Map<String, Object>) map.get("fields"); // fields
            Integer timestampFields = (Integer) map.get("timestamp"); // 시간
            LocalDateTime localDateTime = getLocalDateTime(timestampFields); // 큐에 메시지가 도착한 시간
            mappingService.processMappingInfo(fields, localDateTime); // db저장
        } catch (Exception e) {
            System.err.println("Error Occured in processActiveipLine");
            e.printStackTrace();
        }
    }

    private LocalDateTime getLocalDateTime(Integer timestampFields) {
        Instant instant = Instant.ofEpochSecond(timestampFields);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}


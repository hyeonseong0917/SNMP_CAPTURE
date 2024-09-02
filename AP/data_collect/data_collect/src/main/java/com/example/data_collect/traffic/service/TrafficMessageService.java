package com.example.data_collect.traffic.service;

import com.example.data_collect.interfaceIndex.service.InterfaceIndexProcessingService;
import com.example.data_collect.traffic.db.TrafficEntity;
import com.example.data_collect.traffic.db.TrafficRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrafficMessageService {
    @Autowired
    private TrafficProcessingService trafficProcessingService;

    @Autowired
    private InterfaceIndexProcessingService interfaceIndexProcessingService;

    @Autowired
    private TrafficRepository trafficRepository;

    public void processMessage(String message) {
        List<String> lines = Arrays.asList(message.split("\n")); // 메시지 각 줄
        lines.forEach(line -> processTrafficLine(line)); // 각 줄에 대해 메서드 호출
    }

    @Transactional
    private void processTrafficLine(String line) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(line, Map.class); // 역직렬화
            Map<String, Integer> fields = (Map<String, Integer>) map.get("fields"); // fields
            Map<String, String> tags = (Map<String, String>) map.get("tags"); // tags
            Integer timestampFields = (Integer) map.get("timestamp"); // 시간

            trafficProcessingService.processTrafficInfo(fields, tags, timestampFields); // db 저장
            interfaceIndexProcessingService.processInterfaceIndexInfo(fields,tags,timestampFields); // db 저장
        } catch (Exception e) {
            System.err.println("Erorr Occured in TrafficMsessageService processTrafficLine");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
    }

    private LocalDateTime getLocalDateTime(Integer timestampFields) {
        Instant instant = Instant.ofEpochSecond(timestampFields);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}

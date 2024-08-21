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
//    @Transactional DeadLock?
//    @Transactional
    public void processMessage(String message) {
        List<String> lines = Arrays.asList(message.split("\n")); // 메시지 각 줄
//        try{
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> map = objectMapper.readValue(lines.get(0), Map.class); // 역직렬화
//            Integer timestampFields = (Integer) map.get("timestamp");   // 시간
//            LocalDateTime currentMessageTime=getLocalDateTime(timestampFields); // 현재 메시지 시간
//            Optional<TrafficEntity> latestTraffic = trafficRepository.findFirstByOrderByArrivalTimeDesc();
//            if (latestTraffic.isPresent()) {
//                TrafficEntity traffic = latestTraffic.get();
//                LocalDateTime latestTime=traffic.getArrivalTime(); // db에서 가장 최근 시간
//                Duration duration = Duration.between(latestTime, currentMessageTime);
//                long secondsDifference = duration.getSeconds();
////                System.out.println("cur message time"+currentMessageTime);
////                System.out.println("DB latest time"+latestTime);
////                System.out.println(secondsDifference); // 114450 304
//                if(secondsDifference!=10){
////                    try {
////                        System.out.println("Waiting for 1 second because latestTime is less than 10 seconds greater than currentMessageTime.");
////                        Thread.sleep(1000); // 1초 대기 (q의 개수-1)*1
////                        System.out.println(secondsDifference+"waiting finished");
////                    } catch (InterruptedException e) {
////                        System.err.println("Thread was interrupted!");
////                        Thread.currentThread().interrupt(); // 복구
////                    }
//                }else{
//                    System.out.println("Good");
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("Line 읽는 중 오류 발생");
//        }
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

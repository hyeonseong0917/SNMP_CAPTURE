package com.example.data_collect.status.service;

//import com.example.data_collect.currentstatus.db.CurrentStatusEntity;
//import com.example.data_collect.currentstatus.db.CurrentStatusRepository;
import com.example.data_collect.status.db.StatusEntity;
import com.example.data_collect.status.db.StatusRepository;
//import com.example.data_collect.status.model.StatusRequest;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    private static final String DIRECTORY = "C:\\timestamp";
    private int cnt = 0;
    public void updateStatus(LocalDateTime localDateTime) {
        List<StatusEntity> allCurrentStatuses = readFileAndCreateEntities(localDateTime);
        if (!allCurrentStatuses.isEmpty()) {
            saveStatusEntities(allCurrentStatuses);
        }
    }

    private List<StatusEntity> readFileAndCreateEntities(LocalDateTime localDateTime) {
        List<StatusEntity> allCurrentStatuses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 현재 작업 디렉토리의 위치를 가져옴
        String currentDirectory = System.getProperty("user.dir");
        // 'timestamp' 디렉토리 경로 생성
        File timestampDir = new File(currentDirectory + File.separator + "active_ip");
        String fileName = localDateTime.format(formatter) + ".txt";

        Path filePath=Paths.get(timestampDir.getAbsolutePath(), fileName);
        try {
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath);
                for (String line : lines) {
                    StatusEntity currentStatus = StatusEntity.builder()
                            .connectionStatus(1)
                            .ipAddress(line)
                            .build();
                    allCurrentStatuses.add(currentStatus);
                }
            } else {
                System.out.println("File Not Exist Waiting 1 sec.");
                Thread.sleep(1000); // 1초 대기
                if (Files.exists(filePath)) {
                    List<String> lines = Files.readAllLines(filePath);
                    for (String line : lines) {
                        StatusEntity currentStatus = StatusEntity.builder()
//                                .arrivalTime(localDateTime)
                                .connectionStatus(1)
                                .ipAddress(line)
                                .build();
                        allCurrentStatuses.add(currentStatus);
                    }
                } else {
                    System.out.println("Waiting But File Not Found.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error Occured in File Processing: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error Occured in Thread Waiting: " + e.getMessage());
            Thread.currentThread().interrupt(); // InterruptedException 발생 시 스레드의 인터럽트 상태를 복원
        }

        return allCurrentStatuses;
    }

    @Transactional
    public void saveStatusEntities(List<StatusEntity>  allCurrentStatuses) {
        List<String> currentIps = allCurrentStatuses.stream()
                .map(StatusEntity::getIpAddress)
                .collect(Collectors.toList());

        // 1. 기존 데이터베이스에 있는 모든 StatusEntity를 가져옴
        List<StatusEntity> allStatusEntities = statusRepository.findAll();

        // 2. allCurrentStatuses에 없는 IP 주소를 가진 StatusEntity의 connectionStatus를 0으로 설정
        for (StatusEntity statusEntity : allStatusEntities) {
            System.out.println(statusEntity);
            if (!currentIps.contains(statusEntity.getIpAddress())) {
                statusEntity.setConnectionStatus(0);
                statusRepository.save(statusEntity);
            }
        }

        for (StatusEntity currentStatus : allCurrentStatuses) {
            String currentIp = currentStatus.getIpAddress();
            Optional<StatusEntity> optionalStatusEntity = statusRepository.findTopByIpAddress(currentIp);
            if (optionalStatusEntity.isPresent()) {
                StatusEntity statusEntity = optionalStatusEntity.get();
                statusEntity.setConnectionStatus(1);
                statusRepository.save(statusEntity);
            } else {
                StatusEntity newStatusEntity = StatusEntity.builder()
                        .connectionStatus(1)
                        .ipAddress(currentIp)
                        .build();
                statusRepository.save(newStatusEntity);
            }
        }
    }
}

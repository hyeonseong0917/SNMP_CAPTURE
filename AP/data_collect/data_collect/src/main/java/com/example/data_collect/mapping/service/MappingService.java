package com.example.data_collect.mapping.service;

import com.example.data_collect.common.FieldKey;
//import com.example.data_collect.currentstatus.db.CurrentStatusEntity;
//import com.example.data_collect.currentstatus.db.CurrentStatusRepository;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexEntity;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexId;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexRepository;
import com.example.data_collect.mapping.db.MappingEntity;
import com.example.data_collect.mapping.db.MappingEntityId;
import com.example.data_collect.mapping.db.MappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.io.File;

@Service
@RequiredArgsConstructor
public class MappingService {
    @Autowired
    private InterfaceIndexRepository interfaceIndexRepository;
    @Autowired
    private MappingRepository mappingRepository;

//    @Transactional
    public void processMappingInfo(Map<String, Object> fields, LocalDateTime localDateTime) {
        // 현재 작업 디렉토리의 위치를 가져옴
        String currentDirectory = System.getProperty("user.dir");
        // 'timestamp' 디렉토리 경로 생성
        File timestampDir = new File(currentDirectory + File.separator + "active_ip");

        // 'timestamp' 디렉토리가 존재하는지 확인
        if (!timestampDir.exists()) {
            // 존재하지 않으면 디렉토리 생성
            boolean isCreated = timestampDir.mkdir();

            if (isCreated) {
                System.out.println("'timestamp' directory created successfully.");
            } else {
                System.out.println("Failed to create 'timestamp' directory.");
            }
        } else {
            System.out.println("'timestamp' directory already exists.");
        }
        String DIRECTORY=currentDirectory+"\\active_ip";
        try {
            MappingEntity mappingEntity = getMappingEntity(fields, localDateTime);
            mappingRepository.save(mappingEntity); // mappingEntity를 db에 저장
        } catch (Exception e) {
            System.err.println("Error Occured in processMappingInfo");
            e.printStackTrace();
        }

        // LocalDateTime을 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String fileName = localDateTime.format(formatter) + ".txt";
        Path filePath = Paths.get(DIRECTORY, fileName);

        // ipAdEntIfIndex 값을 추출
        String value = String.valueOf(fields.get("ipAdEntIfIndex"));
        if (value == null) {
            System.err.println("ipAdEntIfIndex is Not In Fields.");
            return;
        }

        try {
            if (Files.exists(filePath)) {
                // 파일이 존재하면, 파일의 마지막 줄에 데이터 추가
                appendDataToFile(filePath, value);
            } else {
                // 파일이 존재하지 않으면, 새 파일을 생성하고 첫 줄에 데이터 추가
                createNewFile(filePath, value);
            }
        } catch (IOException e) {
            System.err.println("Error Occured in File Processing: " + e.getMessage());
        }
    }

    private MappingEntity getMappingEntity(Map<String, Object> fields, LocalDateTime localDateTime) {
        Optional<InterfaceIndexEntity> ifIndexOpt = interfaceIndexRepository.findTopByIfIndex((Integer) fields.get(FieldKey.IP_NET_TO_MEDIA_IF_INDEX.getKey()));
        if(!ifIndexOpt.isPresent()){
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                System.err.println("Thread was interrupted!");
                Thread.currentThread().interrupt(); // 복구
            }
        }
        // 엔티티가 존재하지 않으면 예외 처리
        InterfaceIndexEntity ifIndexEntity = ifIndexOpt.orElseThrow(() -> new RuntimeException("InterfaceIndexEntity not found"));

        // 복합 기본 키 객체 생성
        MappingEntityId mappingEntityId = MappingEntityId.builder()
                .ifName(ifIndexEntity.getIfName()) // `ifName` 필드 설정
                .ipAddress(String.valueOf(fields.get(FieldKey.IP_AD_ENT_IF_INDEX.getKey()))) // `ipAddress` 필드 설정
                .arrivalTime(localDateTime) // `arrivalTime` 필드 설정
                .build();

        // MappingEntity 객체 생성 및 반환
        return MappingEntity.builder()
                .id(mappingEntityId) // 복합 기본 키 객체를 ID로 설정
                .build();
    }


    private void appendDataToFile(Path filePath, String value) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
            writer.newLine(); // 파일의 마지막 줄에 작성
            writer.write(value.toString()); // 데이터 작성
        }
    }

    private void createNewFile(Path filePath, String value) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(value.toString()); // 데이터 작성
        }
    }
}

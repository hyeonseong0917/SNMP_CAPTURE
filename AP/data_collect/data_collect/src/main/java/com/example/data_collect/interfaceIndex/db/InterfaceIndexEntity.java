package com.example.data_collect.interfaceIndex.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name="tb_if_mst")
public class InterfaceIndexEntity {
    @Id
    private String ifName; // 인터페이스 이름
    private Integer ifIndex; // 인터페이스 인덱스
    private LocalDateTime arrivalTime; // 해당 메시지가 큐에 도착한 시간
}

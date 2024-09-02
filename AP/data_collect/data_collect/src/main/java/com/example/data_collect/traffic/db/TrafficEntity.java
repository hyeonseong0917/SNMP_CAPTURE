package com.example.data_collect.traffic.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "tb_if_traffic_hist")
public class TrafficEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traffic_seq; // id
    private String ifName; // 인터페이스 이름
    private Long inErrorPacket; // 수신한 패킷 중 오류 패킷 수
    private Long outErrorPacket; // 송신한 패킷 중 오류 패킷 수
    private Long inPacket; // 수신한 패킷 수
    private Long outPacket; // 송신한 패킷 수
    private Long operStatus; // 운영 상태
    private Long adminStatus; // 관리자 운영 상태
    private LocalDateTime arrivalTime; // 해당 메시지가 큐에 도착한 시간
}

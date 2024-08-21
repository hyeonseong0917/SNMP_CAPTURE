package com.example.data_collect.mapping.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
//@Entity(name = "if_ip_mapping")
@Entity(name = "tb_if_ip_mapping_mst")
public class MappingEntity {
    @EmbeddedId
    private MappingEntityId id;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // id
//    private String ifName; // 인터페이스 이름
//    private String ipAddress; // ip주소
//    private LocalDateTime arrivalTime; // 큐 도착 시간
}


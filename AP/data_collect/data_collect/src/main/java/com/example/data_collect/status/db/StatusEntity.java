package com.example.data_collect.status.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Entity(name = "ip_status")
@Entity(name = "tb_ip_status_mst")
public class StatusEntity {
    @Id
    private String ipAddress; // ip주소
    private Integer connectionStatus; // 연결 상태
}



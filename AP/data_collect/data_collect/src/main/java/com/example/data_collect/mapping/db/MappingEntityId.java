package com.example.data_collect.mapping.db;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MappingEntityId implements Serializable {
    private String ifName;        // 인터페이스 이름
    private String ipAddress;    // IP 주소
    private LocalDateTime arrivalTime; // 큐 도착 시간

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MappingEntityId that = (MappingEntityId) o;
        return Objects.equals(ifName, that.ifName) &&
                Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(arrivalTime, that.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifName, ipAddress, arrivalTime);
    }
}

package com.example.data_collect.traffic.db;

import com.example.data_collect.mapping.db.MappingEntityId;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrafficEntityId {
    private String ifName;        // 인터페이스 이름
    private LocalDateTime arrivalTime; // 큐 도착 시간

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficEntityId that = (TrafficEntityId) o;
        return Objects.equals(ifName, that.ifName) &&
                Objects.equals(arrivalTime, that.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifName, arrivalTime);
    }
}

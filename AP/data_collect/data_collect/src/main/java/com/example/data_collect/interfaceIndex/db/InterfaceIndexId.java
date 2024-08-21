package com.example.data_collect.interfaceIndex.db;

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
public class InterfaceIndexId implements Serializable {
    private String ifName; // 인터페이스 이름
    private Integer ifIndex; // 인터페이스 인덱스
//    private LocalDateTime arrivalTime; // 해당 메시지가 큐에 도착한 시간
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterfaceIndexId that = (InterfaceIndexId) o;
        return Objects.equals(ifName, that.ifName) &&
                Objects.equals(ifIndex, that.ifIndex);
//        return Objects.equals(ifName, that.ifName) &&
//                Objects.equals(ifIndex, that.ifIndex) &&
//                Objects.equals(arrivalTime, that.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifName, ifIndex);
    }
}

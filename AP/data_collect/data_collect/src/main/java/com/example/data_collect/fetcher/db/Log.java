package com.example.data_collect.fetcher.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_syslog_hist")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long syslog_seq;
    private String ipAddress;
    private Date logTime;
    private String hostName;
    private String processName;
    private Integer processId;
    private String content;
    private String severity;

    @Override
    public String toString() {
        return "Log{" +
                "ipAddress='" + ipAddress + '\'' +
                ", logTimestamp=" + logTime +
                ", hostName='" + hostName + '\'' +
                ", processName='" + processName + '\'' +
                ", processId=" + processId +
                ", content='" + content + '\'' +
                ", severity='" + severity + '\'' +
                '}';
    }
}
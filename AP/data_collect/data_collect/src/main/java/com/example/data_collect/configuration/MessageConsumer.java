package com.example.data_collect.configuration;

import com.example.data_collect.fetcher.service.LogProcessingService;
import com.example.data_collect.interfaceIndex.db.InterfaceIndexRepository;
import com.example.data_collect.mapping.db.MappingRepository;
import com.example.data_collect.mapping.service.MappingMessageService;
import com.example.data_collect.status.db.StatusRepository;
import com.example.data_collect.traffic.db.TrafficRepository;
import com.example.data_collect.traffic.service.TrafficMessageService;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private final TrafficRepository trafficRepository;
    private final InterfaceIndexRepository interfaceIndexRepository;
    private final MappingRepository mappingRepository;
    private final StatusRepository statusRepository;
    private final MappingMessageService mappingMessageService;
    private final TrafficMessageService trafficMessageService;
    private final LogProcessingService logProcessingService;
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    long startTime = System.currentTimeMillis();
    long endTime = startTime + (5* 60 * 1000); // 5분 (300초) 후의 시간을 계산

    int processedIpMessages = 0;
    int processedTrafficMessages=0;

    @RabbitListener(queues = "ipIfIndexQueue", containerFactory = "myFactory")
    public void receiveIpMessage(String message){
        mappingMessageService.processMessage(message);
    }

    @RabbitListener(queues = "ifTrafficQueue", containerFactory = "myFactory")
    public void receiveTrafficMessage(String message){
        trafficMessageService.processMessage(message);
    }

    @RabbitListener(queues = "log_queue")
    public void receiveLogMessage(String logMessage) {
        logProcessingService.processLogMessage(logMessage);
    }

}

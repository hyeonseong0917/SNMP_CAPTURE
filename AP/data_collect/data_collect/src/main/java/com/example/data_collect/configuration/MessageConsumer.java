package com.example.data_collect.configuration;

import com.example.data_collect.currentstatus.db.CurrentStatusRepository;
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
import org.springframework.web.reactive.function.client.WebClient;

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
    private final CurrentStatusRepository currentStatusRepository;
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
        long currentTime = System.currentTimeMillis();
        if (currentTime <= endTime) {
            processedIpMessages++;
        } else {
            System.out.println("Processed " + processedIpMessages + " ipIfIndexQueue messages in 5 minutes.");
            try {
                Thread.sleep(10000000);
            }catch(Exception e){
                System.out.println("Thread Error occur");
            }
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println("Current Date and Time: " + formattedNow);
        mappingMessageService.processMessage(message);
        LocalDateTime later=LocalDateTime.now();
        String formattedLater=later.format(formatter);
        System.out.println("Late Date and Time: " + formattedLater);
//        try{
//            Thread.sleep(10000); // 10초 대기
//        }catch (InterruptedException e){
//            System.err.println("Thread was interrupted!");
//        }
    }

    @RabbitListener(queues = "ifTrafficQueue", containerFactory = "myFactory")
    public void receiveTrafficMessage(String message){
        long currentTime = System.currentTimeMillis();
        if (currentTime <= endTime) {
            processedTrafficMessages++;
        } else {
            System.out.println("Processed " + processedTrafficMessages + " Traffic messages in 5 minutes.");
            try {
                Thread.sleep(10000000);
            }catch(Exception e){
                System.out.println("Thread Error occur");
            }
            // 종료 로직 또는 성능 기록
        }
        System.out.println("Processing Traffic message in thread: " + Thread.currentThread().getName());
        DateTimeFormatter formatterMillis = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.NANO_OF_SECOND, 3, 3, true) // 밀리초 단위
                .toFormatter();

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(formatterMillis);
        System.out.println("Current Date and Time (Millis): " + formattedNow);
        // 여기서 메시지의 타임스탬프와 현재 DB의 타임스탬프 비교하면?
        // 메시지의 타임스탬프가 현재 DB의 타임스탬프+10보다 크다면.. 1초 쉬었다가 처리
        trafficMessageService.processMessage(message);
        LocalDateTime later = LocalDateTime.now();
        String formattedLater = later.format(formatterMillis);
        System.out.println("Late Date and Time (Millis): " + formattedLater);
//        Duration duration = Duration.between(now, later);
//        long milliseconds = duration.toMillis(); // 밀리초 단위
//        long seconds = duration.getSeconds(); // 초 단위
//        System.out.println("Duration between times: " + milliseconds + " milliseconds");
//        System.out.println("Duration between times: " + seconds + " seconds");

//        try{
//            Thread.sleep(10000); // 10초 대기
//        }catch (InterruptedException e){
//            System.err.println("Thread was interrupted!");
//        }
    }

    @RabbitListener(queues = "log_queue")
    public void receiveLogMessage(String logMessage) {
        logProcessingService.processLogMessage(logMessage);
    }

}

package com.example.data_collect.fetcher.service;

import com.example.data_collect.fetcher.db.Log;
import com.example.data_collect.fetcher.db.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogProcessingService {

    @Autowired
    private LogParserService logParser;

    @Autowired
    private LogRepository logRepository;

    public void processLogMessage(String logMessage) {
        Log log = logParser.parseLogMessage(logMessage);

        if (log != null) {
            System.out.println("Parsed log: " + log);
            logRepository.save(log);
        } else {
            System.out.println("Failed to parse log: " + logMessage);
        }
    }
}


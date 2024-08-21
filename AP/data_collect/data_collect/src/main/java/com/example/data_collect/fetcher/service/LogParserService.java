package com.example.data_collect.fetcher.service;

import com.example.data_collect.fetcher.db.Log;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogParserService {
    private static final Pattern XML_PATTERN = Pattern.compile(
            "<logCaptureIP>(.*?)</logCaptureIP>" +
                    "<logCaptureTimeStamp>(.*?)</logCaptureTimeStamp>" +
                    "<logCaptureHostName>(.*?)</logCaptureHostName>" +
                    "<logCaptureSyslogTag>(.*?)</logCaptureSyslogTag>" +
                    "<logCaptureMsg>(.*?)</logCaptureMsg>" +
                    "<logCaptureSeverity>(.*?)</logCaptureSeverity>"
    );

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    public Log parseLogMessage(String logMessage) {
        logMessage = logMessage.replaceAll("\\n", "");

        Matcher xmlMatcher = XML_PATTERN.matcher(logMessage);

        if (xmlMatcher.find()) {
            try {
                Log log = new Log();
                log.setIpAddress(xmlMatcher.group(1));
                log.setLogTime(DATE_FORMAT.parse(xmlMatcher.group(2)));
                log.setHostName(xmlMatcher.group(3));
                log.setProcessName(extractProcessName(xmlMatcher.group(4)));
                log.setProcessId(Integer.parseInt(extractProcessId(xmlMatcher.group(4))));
                log.setContent(xmlMatcher.group(5));
                log.setSeverity(xmlMatcher.group(6));
                return log;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String extractProcessName(String syslogTag) {
        int startIndex = syslogTag.indexOf('[');
        return (startIndex != -1) ? syslogTag.substring(0, startIndex) : syslogTag;
    }

    private String extractProcessId(String syslogTag) {
        int startIndex = syslogTag.indexOf('[');
        int endIndex = syslogTag.indexOf(']');
        return (startIndex != -1 && endIndex != -1) ? syslogTag.substring(startIndex + 1, endIndex) : "";
    }
}
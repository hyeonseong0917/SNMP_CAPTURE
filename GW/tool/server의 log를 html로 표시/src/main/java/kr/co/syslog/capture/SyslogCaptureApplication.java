package kr.co.syslog.capture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class SyslogCaptureApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyslogCaptureApplication.class, args);
	}
}


package kr.co.syslog.capture.application;

import kr.co.syslog.capture.domain.SSHService;
import kr.co.syslog.capture.infrastructure.SSHClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SSHServiceImpl implements SSHService {
    private final SSHClient sshClient1;
    private final SSHClient sshClient2;
    private final String COMMAND_SECURE = "cat /var/log/secure";
    private final String COMMAND_MESSAGES = "tail -n 100 /var/log/messages";
    private final String COMMAND_CRON = "cat /var/log/cron";

    public SSHServiceImpl() {
        this.sshClient1 = new SSHClient("192.168.0.231", 22, "codej", "zhemwpdl12!@");
        this.sshClient2 = new SSHClient("192.168.0.232", 22, "codej", "zhemwpdl12!@");
    }

    @Override
    public List<String> getSystemResponse(String command, Integer server) throws Exception {
        SSHClient sshClient;

        if (server == 1)
            sshClient = sshClient1;
        else if (server == 2)
            sshClient = sshClient2;
        else
            throw new RuntimeException("The server you requested does not exist.");

        try {
            String syslog = sshClient.executeCommand(command);
            return Arrays.asList(syslog.split(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve response from system " + server, e);
        }
    }

    @Override
    public List<String> getMessages(Integer server) throws Exception {
        return getSystemResponse(COMMAND_MESSAGES, server);
    }

    public List<String> getSecure(Integer server) throws Exception {
        return getSystemResponse(COMMAND_SECURE, server);
    }

    public List<String> getCron(Integer server) throws Exception {
        return getSystemResponse(COMMAND_CRON, server);
    }

//    // 주기적으로 로그를 요청하는 메소드
//    @Scheduled(fixedRate = 10000) // 60초마다 실행
//    public void requestLogsPeriodically() {
//        try {
//            List<String> messages1 = getMessages(1);
//
//            System.out.println("Server 1 Messages: " + messages1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

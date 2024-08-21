package kr.co.syslog.capture.presentation;

import kr.co.syslog.capture.domain.SSHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SyslogController {
    private final SSHService sshService;

    @Autowired
    public SyslogController(SSHService sshService) {
        this.sshService = sshService;
    }

    @GetMapping("/messages")
    public Map<String, List<String>> getMessages(@RequestParam Integer server) {
        try {
            List<String> syslog = sshService.getMessages(server);
            Map<String, List<String>> response = new HashMap<>();
            response.put("syslog", syslog);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/secure")
    public Map<String, List<String>> getSecure(@RequestParam Integer server) {
        try {
            List<String> syslog = sshService.getSecure(server);
            Map<String, List<String>> response = new HashMap<>();
            response.put("syslog", syslog);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/cron")
    public Map<String, List<String>> getCron(@RequestParam Integer server) {
        try {
            List<String> syslog = sshService.getCron(server);
            Map<String, List<String>> response = new HashMap<>();
            response.put("syslog", syslog);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

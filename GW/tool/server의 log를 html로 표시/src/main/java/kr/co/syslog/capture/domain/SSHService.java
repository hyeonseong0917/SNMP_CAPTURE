package kr.co.syslog.capture.domain;

import java.util.List;

public interface SSHService {
    List<String> getSystemResponse (String command, Integer server) throws Exception;
    List<String> getMessages(Integer server) throws Exception;
    List<String> getSecure(Integer server) throws Exception;
    List<String> getCron(Integer server) throws Exception;
}

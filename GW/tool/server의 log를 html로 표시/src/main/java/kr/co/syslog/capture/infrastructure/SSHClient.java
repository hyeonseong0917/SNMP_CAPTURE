package kr.co.syslog.capture.infrastructure;

import com.jcraft.jsch.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SSHClient {
    private final String host;
    private final int port;
    private final String user;
    private final String password;

    public SSHClient(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String executeCommand(String command) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand("sudo -S " + command);
        channel.setErrStream(System.err);

        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();
        channel.connect();

        // Send the sudo password
        out.write((password + "\n").getBytes(StandardCharsets.UTF_8));
        out.flush();

        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
        }

        channel.disconnect();
        session.disconnect();

        return result.toString();
    }
}

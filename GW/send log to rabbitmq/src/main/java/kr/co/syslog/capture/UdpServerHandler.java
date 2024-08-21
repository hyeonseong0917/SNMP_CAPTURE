package kr.co.syslog.capture;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Sharable
@Component
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger logger = LoggerFactory.getLogger(UdpServerHandler.class);
    private final RabbitMqSender rabbitMqSender;

    public UdpServerHandler(RabbitMqSender rabbitMqSender) {
        this.rabbitMqSender = rabbitMqSender;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String msg = packet.content().toString(CharsetUtil.UTF_8);
        String clientIp = packet.sender().getAddress().getHostAddress();
        transmitLogToRabbitMq(clientIp, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error occurred: ", cause);
        ctx.close();
    }

    private void transmitLogToRabbitMq(String clientIp, String msg) {
        String logMessage = String.format("<logCaptureIP>%s</logCaptureIP>%s", clientIp, msg);
        System.out.println(logMessage);
        rabbitMqSender.send(logMessage);
    }
}

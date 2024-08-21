package kr.co.syslog.capture;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RabbitMqConfig.class)
public class SyslogCaptureApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyslogCaptureApplication.class, args);
	}

	@Bean
	public CommandLineRunner startUdpServer(ApplicationContext ctx) {
		return args -> {
			EventLoopGroup group = new NioEventLoopGroup();
			try {
				Bootstrap b = new Bootstrap();
				b.group(group)
						.channel(NioDatagramChannel.class)
						.handler(new ChannelInitializer<DatagramChannel>() {
							@Override
							public void initChannel(DatagramChannel ch) throws Exception {
								UdpServerHandler udpServerHandler = ctx.getBean(UdpServerHandler.class);
								ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), udpServerHandler);
							}
						})
						.option(ChannelOption.SO_BROADCAST, true);
				
				b.bind(8081).sync().channel().closeFuture().await();
			} finally {
				group.shutdownGracefully();
			}
		};
	}
}

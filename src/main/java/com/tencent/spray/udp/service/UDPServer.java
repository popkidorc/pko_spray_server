package com.tencent.spray.udp.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author kid
 */
@RestController
@RequestMapping("/udp")
public class UDPServer {
	
	private static Logger logger = LoggerFactory.getLogger(UDPServer.class);

	private EventLoopGroup group = new NioEventLoopGroup();

	@Value("${udp.port}")
	private Integer port;

	private Channel channel;

	/**
	 * 发送消息
	 */
	@RequestMapping("/sendMsg")
	public void sendMsg(Integer port, String msg) {
		InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);
		ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getBytes(StandardCharsets.UTF_8));
		channel.writeAndFlush(new DatagramPacket(byteBuf, address));
	}

	@PostConstruct
	public void start() throws InterruptedException {
		logger.info("启动UDP端口：" + port);
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536))
				//缓冲区池化操作
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.handler(new UDPServerHandler());
		//绑定端口，同步等待请求连接
		ChannelFuture cf = bootstrap.bind(port).sync();
		if(cf.isSuccess()) {
			channel = cf.channel();
			logger.info("服务端UDP启动成功，UDP端口：" + port + "监听中。。。");
		}else {
			logger.error("服务UDP端启动失败");
		}
//		cf.channel().closeFuture().await();
	}

	@PreDestroy
	public void destory() throws InterruptedException {
		group.shutdownGracefully().sync();
		System.out.println("关闭Netty UDP");
	}
	

}

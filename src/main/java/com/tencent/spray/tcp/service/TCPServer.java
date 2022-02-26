package com.tencent.spray.tcp.service;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * @author kid
 */
@Component
public class TCPServer {

	private static Logger logger = LoggerFactory.getLogger(TCPServer.class);
	/**
	 * boss 线程组用于处理连接工作
	 */
	private EventLoopGroup boss = new NioEventLoopGroup();
	/**
	 * work 线程组用于数据处理
	 */
	private EventLoopGroup work = new NioEventLoopGroup();

	@Value("${tcp.heartbeatTimeout}")
	private Long heartbeatTimeout;

	@Value("${tcp.port}")
	private Integer port;

	@PostConstruct
	public void start() throws InterruptedException {
		logger.info("启动TCP端口：" + port);

		//EventLoopGroup businessGroup = new NioEventLoopGroup(1050);
		ServerBootstrap bootstrap = new ServerBootstrap();

		//1、创建两个工作线程组：一个用于接收网络请求，另一个用于实际处理业务的线程组
		bootstrap.group(boss,work)
				// 指定Channel
				.channel(NioServerSocketChannel.class)
				//服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
				.option(ChannelOption.SO_BACKLOG, 1024)
				.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536))
				//缓冲区池化操作
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)


				//设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				//将小的数据包包装成更大的帧进行传送，提高网络的负载
				.childOption(ChannelOption.TCP_NODELAY, true)
				//缓存区动态调配（自适应）适用数据包相差不大
				//.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)

				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						//添加编解码
						socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
						socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
	//					socketChannel.pipeline().addLast(new IdleStateHandler(heartbeatTimeout,0,0, TimeUnit.SECONDS));
						socketChannel.pipeline().addLast(new TCPServerHandler());
					}
				});
				//使用指定的端口设置套接字地址
		//绑定端口，同步等待请求连接
		ChannelFuture cf = bootstrap.bind(port).sync();
		if(cf.isSuccess()) {
			logger.info("服务TCP端启动成功，TCP端口："+port+"监听中。。。");
		}else {
			logger.error("服务TCP端启动失败");
		}
//		cf.channel().closeFuture().sync();


//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			//优雅停机
//			bossGroup.shutdownGracefully();
//			workGroup.shutdownGracefully();
//			System.err.println("Server shutDown...");
//		}
	}

	@PreDestroy
	public void destory() throws InterruptedException {
		boss.shutdownGracefully().sync();
		work.shutdownGracefully().sync();
		System.out.println("关闭Netty TCP");
	}
}

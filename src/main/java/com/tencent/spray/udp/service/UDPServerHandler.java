package com.tencent.spray.udp.service;


import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket>{
	
	private static Logger logger = LoggerFactory.getLogger(UDPServerHandler.class);

	private static ConcurrentMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();//管理连接的channel


	public static Channel getChannel(String channelId) {
		Channel channel = channelMap.get(channelId);
		return channel;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {//接收客户端数据
		System.out.println("UDP服务器收到消息: "+msg.content().toString(StandardCharsets.UTF_8));

//		DatagramPacket dp = msg;
//		ByteBuf buf = msg.content();
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String hexStr = ByteBufUtil.hexDump(req);
//		ProtocolData pd=new ProtocolData();
//
//
//		pd.setDatas(hexStr);
//		pd.setCreateTime(new Date());
//		pd.setChannelId(""+ctx.channel().id());
//		pd.setFromCmpany(ConfigBean.vender);
//
//		IMessageFactory myMessageFactory = new MessageFactory();
//	    String replyStr=replyData(myMessageFactory,pd);
//		 if(!"".equals(replyStr)) {
//				 if(replyMessage(ctx,replyStr,dp)){
//
//        		 }else{
//        			 replyMessage(ctx,replyStr,dp);
//        		 }
//		 }
//		 resolveData(myMessageFactory,pd);
	}
	
//	public String replyData(IMessageFactory myMessageFactory,ProtocolData pd) throws Exception {
//		IMessage myMessage = myMessageFactory.createMessage(ConfigBean.vender,pd);
//		String str=myMessage.replyMesage();
//		return str;
//	}


	/**
	 *
	 * 向客户端写数据
	 *
	 * @param ctx
	 * @param content
	 * @param msg
	 * @return
	 * @throws Exception
	 */
    public void sendMessage(ChannelHandlerContext ctx, String content, DatagramPacket msg) throws Exception {
    	if(content == null) {
			return;
		}
		ctx.write(content);
    	ctx.flush();
//    	byte [] resultByte = ByteBufUtil.decodeHexDump(content);
//		ByteBuf encode = Unpooled.buffer(resultByte.length);
//		encode.writeBytes(resultByte);
//		DatagramPacket dp = new DatagramPacket(encode, msg.sender());
//		ctx.writeAndFlush(dp);
    }
    
//	@Async
//	public Future<String> resolveData(IMessageFactory myMessageFactory,ProtocolData pd) throws Exception {
//		IMessage myMessage = myMessageFactory.createMessage(ConfigBean.vender,pd);
//		myMessage.resolveMesage();
//		return new AsyncResult<>("处理完毕");
//	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		channelMap.put(ctx.channel().id().toString(), ctx.channel());
//		InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
//
//		logger.info("--------------"+remoteAddress);
//		String ip=remoteAddress.getAddress().getHostAddress();
//		int port=remoteAddress.getPort();
		logger.info("-----------成功连接UDP服务端------------");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//		channelMap.put(ctx.channel().id().toString(), ctx.channel());
//		InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
//		String ip=remoteAddress.getAddress().getHostAddress();
//		int port=remoteAddress.getPort();
		logger.info("----------与UDP服务器连接中断------------");
	}

	
}

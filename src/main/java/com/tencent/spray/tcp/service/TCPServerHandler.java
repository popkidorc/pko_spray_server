package com.tencent.spray.tcp.service;


import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TCPServerHandler extends ChannelInboundHandlerAdapter{

	private static Logger logger = LoggerFactory.getLogger(TCPServerHandler.class);

	private static ConcurrentMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();//管理连接的channel


	public static Channel getChannel(String channelId) {
		Channel channel = channelMap.get(channelId);
		return channel;
	}

	/**
	 * 接收客户端数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("TCP服务器收到消息: "+msg.toString());

		sendMessage(ctx, "收到TCP，回复"+msg.toString());

//		ByteBuf buf = (ByteBuf) msg;
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String hexStr = ByteBufUtil.hexDump(req);
//		ProtocolData pd=new ProtocolData();
//		InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
//		if(remoteAddress!=null) {
//			String fromIp=remoteAddress.getAddress().getHostAddress();
//			int fromPort=remoteAddress.getPort();
//			pd.setFromIp(fromIp);
//			pd.setFromPort(""+fromPort);
//		}
//		pd.setDatas(hexStr);
//		pd.setCreateTime(new Date());
//		pd.setChannelId(""+ctx.channel().id());
//		pd.setFromCmpany(ConfigBean.vender);
//
//		 IMessageFactory myMessageFactory = new MessageFactory();
//		 String replyStr=replyData(myMessageFactory,pd);
//		 if(!"".equals(replyStr)) {
//				 if(replyMessage(ctx,replyStr)){
//
//        		 }else{
//        			 replyMessage(ctx,replyStr);
//        		 }
//		 }
//		 resolveData(myMessageFactory,pd);

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            ctx.channel().close();//超过心跳时间，关闭连接
            channelMap.remove(ctx.channel().id().toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		channelMap.put(ctx.channel().id().toString(), ctx.channel());
		InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip=remoteAddress.getAddress().getHostAddress();
		int port=remoteAddress.getPort();
		logger.info(ip+":"+port+"-----------成功TCP连接服务端------------");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip=remoteAddress.getAddress().getHostAddress();
		int port=remoteAddress.getPort();
		channelMap.remove(ctx.channel().id().toString());
		logger.info(ip+":"+port+"----------与TCP服务器连接中断------------");
	}



//	public String replyData(IMessageFactory myMessageFactory,ProtocolData pd) throws Exception {
//		IMessage myMessage = myMessageFactory.createMessage(ConfigBean.vender,pd);
//		String str=myMessage.replyMesage();
//		return str;
//	}

	/**
	 * 向客户端写数据
	 *
	 * @param ctx
	 * @param content
	 * @return
	 * @throws Exception
	 */
    public void sendMessage(ChannelHandlerContext ctx, String content) throws Exception {
    	if(content == null) {
			return;
		}
		ctx.write(content);
		ctx.flush();
//    	byte [] resultByte = ByteBufUtil.decodeHexDump(content);
//		ByteBuf encode = Unpooled.buffer(resultByte.length);
//		encode.writeBytes(resultByte);
//		ctx.writeAndFlush(encode);
    }


//	@Async
//	public Future<String> resolveData(IMessageFactory myMessageFactory,ProtocolData pd) throws Exception {
//		IMessage myMessage = myMessageFactory.createMessage(ConfigBean.vender,pd);
//		myMessage.resolveMesage();
//		return new AsyncResult<>("处理完毕");
//	}

}

package com.tencent.spray.controller;//package com.gsafety.fire.iot.controller;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.gsafety.fire.iot.redis.RedisUtils;
//import com.gsafety.fire.iot.server.TCPServerHandler;
//import com.gsafety.fire.iot.util.CRC16M;
//import com.gsafety.fire.iot.util.DateUtil;
//import com.gsafety.fire.iot.util.HexUtil;
//import com.gsafety.fire.iot.util.SpringUtil;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufUtil;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//
//@Api(description="设备下行")
//@RestController
//@RequestMapping("/protocol")
//public class CollectionController {
//
//
//	@PostMapping("/sendSBInspectionCommand")
//	public Map<String,Object> sendInspectionCommand(
//			@ApiParam(value="用户编码") @RequestParam(required = true)
//			String code,
//			@ApiParam(value="厂商") @RequestParam(required = true)
//			String resourceId,
//			@ApiParam(value="漏岗时间（分钟）") @RequestParam(required = true)
//			String cycle
//		    ) {
//		Map<String,Object> map1 = new HashMap<String,Object>();
//		RedisUtils redisUtils = (RedisUtils) SpringUtil.getBean("redisUtils");
//		if(redisUtils.exists(resourceId+"$$"+code)){
//			String value=(String)redisUtils.get(resourceId+"$$"+code);
//			JSONObject job=JSON.parseObject(value);
//			String channelId=job.getString("id");
//			Channel channel = TCPServerHandler.getChannel(channelId);
//			if(channel != null) {
//				String content = "05050505";
//				byte [] resultByte = ByteBufUtil.decodeHexDump(content);
//				ByteBuf encode = Unpooled.buffer(resultByte.length);
//				encode.writeBytes(resultByte);
//				ChannelFuture future = channel.writeAndFlush(encode);
//				if(future.isSuccess()) {
//					map1.put("statue", "1");
//				}else {
//					map1.put("statue", "2");
//				}
//			}else {
//				map1.put("statue", "3");
//			}
//		}else{
//			map1.put("statue", "3");
//		}
//		return map1;
//	}
//
//
//}

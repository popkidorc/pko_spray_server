package com.tencent.spray.controller;

import com.tencent.spray.tcp.service.TCPServer;
import com.tencent.spray.udp.service.UDPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestWebController {

    private static Logger logger = LoggerFactory.getLogger(TCPServer.class);

    @Autowired
    private TCPServer tcpServer;


    @Autowired
    private UDPServer udpServer;

    @RequestMapping("/name")
    public String name(String name) {
        logger.info("测试通过！"+name);
        return "ok";
    }

    @RequestMapping("/test")
    public String start() throws InterruptedException {
        tcpServer.start();

        udpServer.start();
        return "ok";
    }

}

package com.tencent.spray.controller;

import com.tencent.spray.tcp.service.TCPServer;
import com.tencent.spray.udp.service.UDPServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestWebController {

    @Autowired
    private TCPServer tcpServer;


    @Autowired
    private UDPServer udpServer;

    @RequestMapping("/name")
    public String name(String name) {
        System.out.println("测试通过！"+name);
        return "ok";
    }

    @RequestMapping("/test")
    public String test() {
        System.out.println("测试通过!！");
        return "ok";
    }

}

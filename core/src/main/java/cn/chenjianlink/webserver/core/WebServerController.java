package cn.chenjianlink.webserver.core;

import java.util.Scanner;

/**
 * Web服务器控制器
 */
public class WebServerController {

    public static void run() {
        //启动服务器
        Server server = Server.getInstance();
        server.start(9090);
    }
}

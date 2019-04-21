package cn.chenjianlink.webserver.core;


import cn.chenjianlink.webserver.core.utils.PropertiesUtils;

/**
 * Web服务器控制器
 */
public class WebServerController {

    public static void run() {
        //启动服务器
        Server server = Server.getInstance();
        int port = PropertiesUtils.getPort();
        server.start(port);
        server.receive();
    }
}

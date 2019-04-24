package cn.chenjianlink.webserver.core;


import cn.chenjianlink.webserver.core.utils.PropertiesUtils;

import java.util.Scanner;

/**
 * Web服务器控制器
 *
 * @author chenjian
 */
public class WebServerController {

    private static final String STATUS = "EXIT";

    public static void run() {
        //启动服务器
        Server server = Server.getInstance();
        int port = PropertiesUtils.getPort();
        Scanner scanner = new Scanner(System.in);
        server.start(port);
        String state;
        while (scanner.hasNext()) {
            state = scanner.next();
            if (state.equals(STATUS)) {
                server.stop();
                System.exit(0);
            }
        }
    }
}

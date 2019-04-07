package cn.chenjianlink.webserver.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * server
 */
public class Server {
    private ServerSocket serverSocket;
    private boolean isRunning;
    private static final int DEFAULT_PORT = 8080;

    private final Log logger = LogFactory.getLog(Server.class);

    //使用单例模式加载server类
    private static class ServerInstance {
        private static final Server instance = new Server();
    }

    public static Server getInstance() {
        return ServerInstance.instance;
    }

    private Server() {
    }

    //启动服务
    public void start(Integer port) {
        //若没有设置端口号则使用默认端口
        if (port == null) {
            port = DEFAULT_PORT;
        }
        try {
            logger.info("服务器启动中......");
            serverSocket = new ServerSocket(port);
            isRunning = true;
            logger.info("服务器已启动......");
            receive();
        } catch (IOException e) {
            logger.error("服务器启动失败....", e);
            e.printStackTrace();
            stop();
        }
    }

    //接受连接处理
    public void receive() {
        while (isRunning) {
            try {
                Socket client = serverSocket.accept();
                logger.info("一个客户端建立了连接....");
                //多线程处理
                new Thread(new Dispatcher(client)).start();
            } catch (IOException e) {
                logger.error("客户端错误", e);
                e.printStackTrace();
            }
        }
    }

    //停止服务
    public void stop() {
        isRunning = false;
        try {
            this.serverSocket.close();
            logger.info("服务器已停止");
        } catch (IOException e) {
            logger.error("服务器关闭异常", e);
            e.printStackTrace();
        }
    }
}

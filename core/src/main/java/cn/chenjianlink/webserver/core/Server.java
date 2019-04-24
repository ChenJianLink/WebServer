package cn.chenjianlink.webserver.core;

import cn.chenjianlink.webserver.core.context.Dispatcher;
import cn.chenjianlink.webserver.core.context.WebApp;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器运行
 * @author chenjian
 */
@Slf4j
public class Server {
    private ServerSocket serverSocket;
    /**
     * 默认端口
     */
    private static final int DEFAULT_PORT = 8080;

    private Accepter accepter;

    /**
     * 使用单例模式加载server类
     */
    private static class ServerInstance {
        private static final Server INSTANCE = new Server();
    }

    public static Server getInstance() {
        return ServerInstance.INSTANCE;
    }

    private Server() {
    }


    /**
     * 启动服务
     *
     * @param port
     */
    public void start(Integer port) {
        //若没有设置端口号则使用默认端口
        if (port == null) {
            port = DEFAULT_PORT;
        }
        try {
            log.info("服务器启动中......");
            serverSocket = new ServerSocket(port);
            WebApp.init();
            accepter = new Accepter();
            accepter.start();
            log.info("服务器已启动......");
        } catch (Exception e) {
            log.error("服务器启动失败....", e);
            e.printStackTrace();
            stop();
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        accepter.shutdown();
        log.info("服务器已停止");
    }

    /**
     * 请求接收器
     */
    private class Accepter extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket client = serverSocket.accept();
                    log.info("一个客户端建立了连接....");
                    //多线程处理
                    new Thread(new Dispatcher(client)).start();
                } catch (IOException e) {
                    log.error("客户端错误", e);
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                log.error("服务器关闭异常", e);
                e.printStackTrace();
            } finally {
                super.interrupt();
            }
        }

        /**
         * 关闭请求接收器
         */
        public void shutdown() {
            Thread.currentThread().interrupt();
        }
    }
}


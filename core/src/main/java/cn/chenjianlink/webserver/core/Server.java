package cn.chenjianlink.webserver.core;

import cn.chenjianlink.webserver.core.context.Dispatcher;
import cn.chenjianlink.webserver.core.context.WebApp;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器运行
 *
 * @author chenjian
 */
@Slf4j
public class Server {
    private ServerSocket serverSocket;
    /**
     * 默认端口
     */
    private final int DEFAULT_PORT = 8080;

    private final int DEFAULT_THREADS = 50;

    private final int MAX_THREADS = 150;

    private ExecutorService executor;

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
    public void start(Integer port, Integer threads) {
        //若没有设置端口号则使用默认端口
        if (port == null) {
            port = DEFAULT_PORT;
        }
        if (threads == null) {
            threads = DEFAULT_THREADS;
        }
        //设置最大连接数
        if (threads > MAX_THREADS) {
            threads = MAX_THREADS;
        }
        try {
            log.info("服务器启动中......");
            serverSocket = new ServerSocket(port);
            WebApp.init();
            executor = Executors.newFixedThreadPool(threads);
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
        try {
            if (accepter != null) {
                accepter.interrupt();
            }
            if (executor != null) {
                executor.shutdown();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            log.info("服务器已停止");
        } catch (IOException e) {
            log.error("服务器关闭异常", e);
            e.printStackTrace();
        }

    }

    /**
     * 请求接收器
     */
    private class Accepter extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && !executor.isShutdown()) {
                try {
                    Socket client = serverSocket.accept();
                    log.info("一个客户端建立了连接....");
                    //多线程处理
                    executor.execute(new Dispatcher(client));
                } catch (IOException e) {
                    log.error("客户端错误", e);
                    e.printStackTrace();
                }
            }
        }
    }
}


package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * 分发器：加入状态内容处理
 */
@Slf4j
public class Dispatcher implements Runnable {
    private Socket client;
    private Request request;
    private Response response;
    private RequestHandler requestHandler;

    public Dispatcher(Socket client) {
        this.client = client;
        try {
            //获取请求协议
            request = new Request(client);
            //获取响应协议
            response = new Response(client);
            //获取请求处理器
            requestHandler = new RequestHandler();
        } catch (IOException e) {
            log.error("", e);
            e.printStackTrace();
            this.release();
        }
    }

    @Override
    public void run() {
        try {
            log.info("开始处理请求");
            requestHandler.processRequest(request, response);
            log.info("请求处理完毕");
        } catch (IOException e) {
            log.error("", e);
            e.printStackTrace();
        } finally {
            release();
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        try {
            client.close();
            log.info("关闭客户端连接");
        } catch (IOException e) {
            log.error("连接关闭异常", e);
            e.printStackTrace();
        }
    }


}

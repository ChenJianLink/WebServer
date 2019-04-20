package cn.chenjianlink.webserver.core;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.context.WebApp;
import cn.chenjianlink.webserver.core.response.Response;
import cn.chenjianlink.webserver.core.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 分发器：加入状态内容处理
 */
@Slf4j
public class Dispatcher implements Runnable {
    private Socket client;
    private Request request;
    private Response response;

    private String index = "/";

    public Dispatcher(Socket client) {
        this.client = client;
        try {
            //获取请求协议
            request = new Request(client);
            //获取响应协议
            response = new Response(client);
        } catch (IOException e) {
            e.printStackTrace();
            this.release();
        }
    }

    @Override
    public void run() {

        try {
            if (null == request.getUrl() || request.getUrl().equals("")) {
                response.pushToBrowser(200);
                return;
            }
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            if (null != servlet) {
                servlet.service(request, response);
                //关注了状态码
                response.pushToBrowser(200);
            } else {
                //
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("404.html");
                String html = inputStreamToSring(is);
                response.println(html);
                response.pushToBrowser(404);
            }
        } catch (Exception e) {
            try {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("500.html");
                String html = inputStreamToSring(is);
                response.println(html);
                response.pushToBrowser(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        release();
    }

    /**
     * 释放资源
     */
    private void release() {
        try {
            client.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 读取页面并返回页面
     */
    private String inputStreamToSring(InputStream inputStream) {
        if (inputStream != null) {
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String tempOneLine;
                while ((tempOneLine = bf.readLine()) != null) {
                    sb.append(tempOneLine);
                }
                return sb.toString();
            } catch (IOException e) {
                log.error("页面读取错误", e);
                e.printStackTrace();
            }

        }
        return null;
    }

}

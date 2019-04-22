package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;
import cn.chenjianlink.webserver.core.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 请求处理器
 *
 * @author chenjian
 */
@Slf4j
public class RequestHandler {

    private final String EMPTY = "";
    private final String STATIC = ".";

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

    public void processRequest(Request request, Response response) throws IOException {
        try {
            //处理静态资源请求
            if (request.getUrl() != null && request.getUrl().contains(STATIC)) {

            } else {
                //如果不是静态资源
                /*
                 * 请求不带任何参数以及路径返回默认首页
                 */
                if (request.getUrl() == null || request.getUrl().equals(EMPTY)) {
                    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
                    String html = inputStreamToSring(is);
                    response.println(html);
                    response.write();
                } else {
                    Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
                    if (null != servlet) {
                        servlet.service(request, response);
                        //将内容输出
                        response.write();
                    } else {
                        //输出404
                        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("404.html");
                        String html = inputStreamToSring(is);
                        response.println(html);
                        response.setStatus(404);
                        response.write();
                    }
                }
            }
        } catch (IOException e) {
            log.error("服务器内部错误", e);
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("500.html");
            String html = inputStreamToSring(is);
            response.println(html);
            response.setStatus(500);
            response.write();
        }
    }

}

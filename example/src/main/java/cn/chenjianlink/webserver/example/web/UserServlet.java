package cn.chenjianlink.webserver.example.web;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;
import cn.chenjianlink.webserver.core.servlet.HttpServlet;

import java.io.IOException;

public class UserServlet extends HttpServlet {
    @Override
    public void service(Request request, Response response) {
        response.println("用户表");
        try {
            response.pushToBrowser(200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

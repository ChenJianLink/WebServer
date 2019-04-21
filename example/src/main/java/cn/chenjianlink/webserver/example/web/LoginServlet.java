package cn.chenjianlink.webserver.example.web;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;
import cn.chenjianlink.webserver.core.servlet.HttpServlet;
import cn.chenjianlink.webserver.core.servlet.Servlet;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    public void service(Request request, Response response) {
        response.println("请登录");
        try {
            response.pushToBrowser(200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(Request request, Response response) {

    }

    @Override
    public void doPost(Request request, Response response) {

    }
}

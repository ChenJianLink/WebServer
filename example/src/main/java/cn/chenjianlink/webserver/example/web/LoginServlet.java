package cn.chenjianlink.webserver.example.web;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;
import cn.chenjianlink.webserver.core.servlet.HttpServlet;


/**
 * @author chenjian
 */
public class LoginServlet extends HttpServlet {


    @Override
    public void doGet(Request request, Response response) {
        response.println("thinking");
    }

    @Override
    public void doPost(Request request, Response response) {

    }
}

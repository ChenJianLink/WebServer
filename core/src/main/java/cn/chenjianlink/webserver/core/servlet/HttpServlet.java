package cn.chenjianlink.webserver.core.servlet;

import cn.chenjianlink.webserver.core.enumeration.HttpMethod;
import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;

/**
 * HttpServlet实现类，处理Http请求直接继承该类
 *
 * @author chenjian
 */
public class HttpServlet implements Servlet {

    @Override
    public void service(Request request, Response response) {
        if (request.getMethod().equals(HttpMethod.GET.toString())){
            doGet(request, response);
        }else if (request.getMethod().equals(HttpMethod.POST.toString())){
            doPost(request, response);
        }
    }

    @Override
    public void doGet(Request request, Response response) {
    }

    @Override
    public void doPost(Request request, Response response) {
    }
}

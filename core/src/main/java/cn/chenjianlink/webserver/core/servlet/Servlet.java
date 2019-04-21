package cn.chenjianlink.webserver.core.servlet;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;

/**
 * 服务器接口
 *
 * @author chenjian
 */
public interface Servlet {
    /**
     * 提供web服务
     *
     * @param request
     * @param response
     */
    void service(Request request, Response response);

    /**
     * 处理get请求
     * @param request
     * @param response
     */
    void doGet(Request request, Response response);

    /**
     * 处理post请求
     * @param request
     * @param response
     */
    void doPost(Request request, Response response);
}

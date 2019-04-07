package cn.chenjianlink.webserver.core.servlet;

import cn.chenjianlink.webserver.core.Request.Request;
import cn.chenjianlink.webserver.core.response.Response;

/**
 * 服务器接口
 *
 */
public interface Servlet {
	void service(Request request, Response response);
}

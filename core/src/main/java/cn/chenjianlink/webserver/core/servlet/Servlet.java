package cn.chenjianlink.webserver.core.servlet;

import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;

/**
 * 服务器接口
 * @author chenjian
 */
public interface Servlet {
	/**
	 * 提供web服务
	 * @param request
	 * @param response
	 */
	void service(Request request, Response response);
}

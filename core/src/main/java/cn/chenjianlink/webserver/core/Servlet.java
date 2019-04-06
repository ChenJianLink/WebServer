package cn.chenjianlink.webserver.core;
/**
 * 服务器接口
 *
 */
public interface Servlet {
	void service(Request request, Response response);
}

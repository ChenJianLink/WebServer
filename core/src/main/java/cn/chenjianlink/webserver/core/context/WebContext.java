package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.servlet.Servlet;

import java.util.HashMap;
import java.util.Map;

/**
 * web容器
 */
public class WebContext {

    /**
     * key -->url-pattern value -->servlet-name 一对多关系
     */
    private Map<String, String> servletMapping = new HashMap<String, String>();

    /**
     * 存储对应的servlet
     */
    private Map<String, Servlet> servletMap = new HashMap<String, Servlet>();

    /**
     * 存储servlet
     * @param servletName
     * @param servlet
     */
    public void setServlet(String servletName, Servlet servlet) {
        servletMap.put(servletName, servlet);
    }

    /**
     * 获取servlet实例
     * @param pattern
     * @return
     */
    public Servlet getServlet(String pattern) {
        String name = servletMapping.get(pattern);
        return servletMap.get(name);
    }

    public void setServletMappingValue(String servletName, String urlPattern) {
        this.servletMapping.put(urlPattern, servletName);
    }



}

package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.servlet.Servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web容器
 * @author chenjian
 */
public class WebContext {

    /**
     * 错误页面存储容器
     */
    private Map<String, String> errorPage = new HashMap<>();
    /**
     * key -->url-pattern value -->servlet-name 一对多关系
     */
    private Map<String, String> servletMapping = new HashMap<>();

    /**
     * 存储对应的servlet
     */
    private Map<String, Servlet> servletMap = new HashMap<>();

    /**
     * 首页存储
     */
    private List<String> welcomeFileList = new ArrayList<>();

    /**
     * 存储servlet
     *
     * @param servletName
     * @param servlet
     */
    public void setServlet(String servletName, Servlet servlet) {
        servletMap.put(servletName, servlet);
    }

    /**
     * 获取servlet实例
     *
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

    public void setErrorPage(String errorCode, String location) {
        errorPage.put(errorCode, location);
    }

    public String getErrorPage(Integer errorCode) {
        return errorPage.get(errorCode.toString());
    }

    public void setWelcomeFileList(String welcomeFileUrl) {
        welcomeFileList.add(welcomeFileUrl);
    }

    public String[] getWelcomeFileList() {
        return welcomeFileList.toArray(new String[welcomeFileList.size()]);
    }
}

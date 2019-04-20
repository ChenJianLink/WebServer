package cn.chenjianlink.webserver.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * web容器
 */
public class WebContext {
    /**
     * key-->servlet-name  value -->servlet-class
     */
    private Map<String, String> servlet = new HashMap<String, String>();

    /**
     * key -->url-pattern value -->servlet-name 一对多关系
     */
    private Map<String, String> servletMapping = new HashMap<String, String>();

    public void setServletValue(String servletName, String servletClass) {
        this.servlet.put(servletName, servletClass);
    }

    public void setServletMappingValue(String servletName, String urlPattern) {
        this.servletMapping.put(urlPattern, servletName);
    }

    /**
     * 通过URL的路径找到了对应class
     *
     * @param pattern
     * @return
     */
    public String getClazz(String pattern) {
        String name = servletMapping.get(pattern);
        return servlet.get(name);
    }

}

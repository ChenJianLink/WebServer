package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

/**
 * 解析web.xml并储存内容
 */
@Slf4j
public class WebApp {
    private static WebContext webContext = new WebContext();

    private static final String SERVLET = "servlet";

    private static final String SERVLET_NAME = "servlet-name";

    private static final String SERVLET_CLASS = "servlet-class";

    private static final String SERVLET_MAPPING = "servlet-mapping";

    private static final String URL_PATTERN = "url-pattern";

    //对web.xml解析
    static {
        try {
            log.info("开始解析web.xml文件");
            //dom解析
            SAXReader reader = new SAXReader();
            Document document = reader.read(WebApp.class.getResourceAsStream("/web.xml"));
            //获取根节点
            Element root = document.getRootElement();
            //解析元素
            List<Element> servlets = root.elements(SERVLET);
            for (Element serlvet : servlets) {
                String servletName = serlvet.element(SERVLET_NAME).getText();
                String servletClass = serlvet.element(SERVLET_CLASS).getText();
                webContext.setServletValue(servletName, servletClass);
            }
            List<Element> servletMappings = root.elements(SERVLET_MAPPING);
            for (Element serlvetMapping : servletMappings) {
                String servletName = serlvetMapping.element(SERVLET_NAME).getText();
                String urlPattern = serlvetMapping.element(URL_PATTERN).getText();
                webContext.setServletMappingValue(servletName, urlPattern);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error("解析配置文件错误", e);
        }
    }

    /**
     * 通过url获取配置文件对应的servlet
     *
     * @param url
     * @return
     */
    public static Servlet getServletFromUrl(String url) {
        String className = webContext.getClazz("/" + url);
        Class clazz;
        try {
            log.info("请求路径：" + url + "-->" + "对应的servlet:" + className);
            clazz = Class.forName(className);
            Servlet servlet = (Servlet) clazz.newInstance();
            return servlet;
        } catch (ClassNotFoundException e) {
            log.error("未找到对应的servlet", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.error("解析加载错误", e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
            log.error("解析加载错误", e);
        }
        return null;
    }
}

package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.servlet.Servlet;
import lombok.extern.log4j.Log4j;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 解析web.xml的相关类
 */
@Log4j
public class WebApp {
    private static WebContext webContext;
    //日志处理

    static {
        try {
            log.info("开始解析web.xml文件");
            //SAX解析
            //1、获取解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //2、从解析工厂获取解析器
            SAXParser parse = factory.newSAXParser();
            //3、编写处理器
            //4、加载文档 Document 注册处理器
            WebHandler handler = new WebHandler();
            //5、解析
            parse.parse(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream("web.xml")
                    , handler);
            //获取数据
            webContext = new WebContext(handler.getEntitys(), handler.getMappings());
        } catch (Exception e) {
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
        String className = webContext.getClz("/" + url);
        Class clazz;
        try {
            log.info(url + "-->" + className + "-->");
            clazz = Class.forName(className);
            Servlet servlet = (Servlet) clazz.getConstructor().newInstance();
            return servlet;
        } catch (Exception e) {

        }
        return null;
    }
}

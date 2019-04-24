package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.exception.PageNotFoundException;
import cn.chenjianlink.webserver.core.exception.ServerStartException;
import cn.chenjianlink.webserver.core.servlet.Servlet;
import cn.chenjianlink.webserver.core.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

/**
 * 解析web.xml并储存内容
 *
 * @author chenjian
 */
@Slf4j
public class WebApp {
    private static final WebContext WEBCONTEXT = new WebContext();

    private final String SERVLET = "servlet";

    private final String SERVLET_NAME = "servlet-name";

    private final String SERVLET_CLASS = "servlet-class";

    private final String SERVLET_MAPPING = "servlet-mapping";

    private final String URL_PATTERN = "url-pattern";

    private final String ERROR_PAGE = "error-page";

    private final String ERROR_CODE = "error-code";

    private final String LOCATION = "location";

    private final String WELCOME_FILE_LIST = "welcome-file-list";

    private final String WELCOME_FILE = "welcome-file";

    /**
     * 对web.xml进行解析
     */
    public static void init() throws ServerStartException {
        WebApp webApp = new WebApp();
        try {
            log.info("开始解析web.xml文件");
            //dom解析
            SAXReader reader = new SAXReader();
            Document document = reader.read(WebApp.class.getResourceAsStream("/web.xml"));
            //获取根节点
            Element root = document.getRootElement();
            webApp.analysisServlet(root);
            webApp.analysisServletMapping(root);
            webApp.analysisErrorPage(root);
            webApp.analysisWelcomeFileList(root);
            log.info("web.xml解析完成");
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error("解析web.xml文件错误", e);
            throw new ServerStartException("解析web.xml文件错误");
        } catch (ReflectiveOperationException e) {
            log.error("创建servlet实例异常", e);
            e.printStackTrace();
            throw new ServerStartException("创建servlet实例异常");
        }
    }

    /**
     * 解析servlet元素
     *
     * @param element
     * @throws ReflectiveOperationException
     */
    private void analysisServlet(Element element) throws ReflectiveOperationException {
        List<Element> servlets = element.elements(SERVLET);
        for (Element serlvet : servlets) {
            String servletName = serlvet.element(SERVLET_NAME).getText();
            String servletClass = serlvet.element(SERVLET_CLASS).getText();
            Servlet servletClazz = (Servlet) Class.forName(servletClass).newInstance();
            WEBCONTEXT.setServlet(servletName, servletClazz);
        }
    }

    /**
     * 解析servlet-mapping元素
     *
     * @param element
     */
    private void analysisServletMapping(Element element) {
        List<Element> servletMappings = element.elements(SERVLET_MAPPING);
        for (Element serlvetMapping : servletMappings) {
            String servletName = serlvetMapping.element(SERVLET_NAME).getText();
            String urlPattern = serlvetMapping.element(URL_PATTERN).getText();
            WEBCONTEXT.setServletMappingValue(servletName, urlPattern);
        }
    }

    /**
     * 解析error-page元素
     *
     * @param element
     */
    private void analysisErrorPage(Element element) {
        List<Element> errorPages = element.elements(ERROR_PAGE);
        for (Element errorPage : errorPages) {
            String errorCode = errorPage.element(ERROR_CODE).getText();
            String location = errorPage.element(LOCATION).getText();
            WEBCONTEXT.setErrorPage(errorCode, location);
        }
    }

    /**
     * 解析welcome-file-list元素
     *
     * @param element
     */
    private void analysisWelcomeFileList(Element element) {
        Element welcomeFileList = element.element(WELCOME_FILE_LIST);
        List<Element> welcomeFiles = welcomeFileList.elements(WELCOME_FILE);
        for (Element welcomeFile : welcomeFiles) {
            String welcomFileName = welcomeFile.getText();
            WEBCONTEXT.setWelcomeFileList(welcomFileName);
        }
    }

    /**
     * 通过url获取配置文件对应的servlet
     *
     * @param url
     * @return
     */
    public static Servlet getServletFromUrl(String url) {
        Servlet servlet = WEBCONTEXT.getServlet("/" + url);
        log.info(url + "-->" + "获取对应的servlet:" + (servlet == null ? null : servlet.getClass().getName()));
        return servlet;
    }

    /**
     * 私有化构造器
     */
    private WebApp() {
    }

    /**
     * 获取首页
     *
     * @return
     */
    public static String getWelcomeFiles() {
        String[] welcomeFileList = WEBCONTEXT.getWelcomeFileList();
        for (String welcomeFile : welcomeFileList) {
            if (IOUtils.isFileExist(welcomeFile)) {
                return welcomeFile;
            }
        }
        return null;
    }

    /**
     * 获取错误页面
     *
     * @param code
     * @return
     */
    public static String getErrorPage(int code) throws PageNotFoundException {
        String errorPageUrl = WEBCONTEXT.getErrorPage(code);
        if (!IOUtils.isFileExist(errorPageUrl)) {
            throw new PageNotFoundException("读取错误页面异常，" + errorPageUrl + "该路径的目标文件不存在");
        }
        return errorPageUrl;
    }
}

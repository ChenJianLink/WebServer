package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.comment.GeneralResources;
import cn.chenjianlink.webserver.core.enumeration.HttpStatus;
import cn.chenjianlink.webserver.core.exception.PageNotFoundException;
import cn.chenjianlink.webserver.core.exception.ServletNotFoundException;
import cn.chenjianlink.webserver.core.exception.StaticResourceNotFoundException;
import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;
import cn.chenjianlink.webserver.core.servlet.Servlet;
import cn.chenjianlink.webserver.core.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

import static cn.chenjianlink.webserver.core.enumeration.HttpStatus.*;

/**
 * 请求处理器
 *
 * @author chenjian
 */
@Slf4j
public class RequestHandler {

    private static final String STATIC = ".";
    private static final String HTML_SUFFIX = ".html";
    private static final String DEFAULT_NOT_FOUND_PAGE = "error/404.html";
    private static final String DEFAULT_ERROR_PAGE = "error/500.html";
    private static final String DEFAULT_INDEX_PAGE = "index.html";

    private ResourcesHandler resourcesHandler;

    /**
     * 请求处理方法
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void processRequest(Request request, Response response) throws IOException {
        try {
            //处理静态资源请求
            if (request.getUrl() != null && request.getUrl().contains(STATIC)) {
                resourcesHandler = new ResourcesHandler();
                if (!request.getUrl().endsWith(HTML_SUFFIX)) {
                    resourcesHandler.findStaticResources(request, response);
                } else {
                    resourcesHandler.findHtmlPage(request, response);
                }
            } else {
                //如果不是静态资源
                /*
                 * 请求不带任何参数以及路径返回默认首页
                 */
                if (request.getUrl() == null || request.getUrl().equals(GeneralResources.EMPTY)) {
                    printIndexPage(response);
                } else {
                    doService(request, response);
                }
            }
        } catch (IOException e) {
            log.error("服务器内部错误", e);
            e.printStackTrace();
            printErrorPage(response, SERVER_ERROR);
        } catch (ServletNotFoundException e) {
            log.error("没有找到对应的servlet", e);
            e.printStackTrace();
            //输出404
            printErrorPage(response, NOT_FOUND);
        } catch (PageNotFoundException e) {
            log.error("没有找到对应的页面", e);
            e.printStackTrace();
            //输出404
            printErrorPage(response, NOT_FOUND);
        } catch (StaticResourceNotFoundException e) {
            log.error("没有找到对应的静态资源", e);
            e.printStackTrace();
            //输出404
            printErrorPage(response, NOT_FOUND);
        }
    }

    /**
     * 获取首页
     *
     * @return
     * @throws IOException
     */
    private void printIndexPage(Response response) throws IOException {
        log.info("访问首页");
        InputStream inputStream;
        //获取首页输入流
        String welcomeFiles = WebApp.getWelcomeFiles();
        if (welcomeFiles != null) {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(welcomeFiles);
        } else {
            log.info("未找到自定义的首页");
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_INDEX_PAGE);
        }
        String html = IOUtils.inputStreamToSring(inputStream);
        response.println(html);
        response.write();
    }


    /**
     * servlet处理封装
     *
     * @param request
     * @param response
     */
    private void doService(Request request, Response response) throws ServletNotFoundException, IOException {
        Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
        if (null != servlet) {
            servlet.service(request, response);
            //将内容输出
            response.write();
        } else {
            throw new ServletNotFoundException("没有与" + request.getUrl() + "对应的servlet");
        }
    }

    /**
     * 输出错误页面
     */
    private void printErrorPage(Response response, HttpStatus status) throws IOException {
        InputStream is;
        String errorPage = null;
        try {
            errorPage = WebApp.getErrorPage(status.getCode());
        } catch (PageNotFoundException e) {
            log.error("没有找到对应错误的页面", e);
            e.printStackTrace();
        }

        if (errorPage != null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(errorPage);
        } else {
            if (status.getCode() == NOT_FOUND.getCode()) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_NOT_FOUND_PAGE);
            } else {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_ERROR_PAGE);
            }
        }
        String html = IOUtils.inputStreamToSring(is);
        response.println(html);
        response.setHeader(status);
        response.write();
    }


}

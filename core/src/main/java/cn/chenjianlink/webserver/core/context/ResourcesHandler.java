package cn.chenjianlink.webserver.core.context;

import cn.chenjianlink.webserver.core.exception.PageNotFoundException;
import cn.chenjianlink.webserver.core.exception.StaticResourceNotFoundException;
import cn.chenjianlink.webserver.core.request.Request;
import cn.chenjianlink.webserver.core.response.Response;
import cn.chenjianlink.webserver.core.utils.IOUtils;
import cn.chenjianlink.webserver.core.utils.MimeTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

import static cn.chenjianlink.webserver.core.enumeration.HttpStatus.OK;

/**
 * 静态资源处理类
 *
 * @author chenjian
 */
@Slf4j
public class ResourcesHandler {

    private static final String ICO = ".ico";

    /**
     * 获取静态资源
     *
     * @param request
     * @param response
     */
    public void findStaticResources(Request request, Response response) throws IOException, StaticResourceNotFoundException {
        log.info("处理静态资源");
        //判断对应的文件是否存在
        if (!IOUtils.isFileExist(request.getUrl())) {
            throw new StaticResourceNotFoundException();
        }
        String contentType;
        if (request.getUrl().endsWith(ICO)) {
            contentType = "image/x-icon";
        } else {
            contentType = MimeTypeUtil.findFileType(request.getUrl());
        }
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(request.getUrl());
        response.createbody(IOUtils.toByteArray(inputStream));
        response.setHeader(OK, contentType);
        response.write();
    }

    /**
     * 获取页面
     *
     * @param request
     * @param response
     * @throws PageNotFoundException
     */
    public void findHtmlPage(Request request, Response response) throws PageNotFoundException, IOException {
        log.info("访问静态页面");
        //判断对应的文件是否存在
        if (!IOUtils.isFileExist(request.getUrl())) {
            throw new PageNotFoundException();
        }
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(request.getUrl());
        String html = IOUtils.inputStreamToSring(inputStream);
        response.println(html);
        response.setHeader(OK);
        response.write();
    }

}

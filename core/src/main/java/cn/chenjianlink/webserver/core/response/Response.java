package cn.chenjianlink.webserver.core.response;

import cn.chenjianlink.webserver.core.enumeration.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import static cn.chenjianlink.webserver.core.comment.GeneralResources.CRLF;
import static cn.chenjianlink.webserver.core.enumeration.HttpStatus.OK;
import static cn.chenjianlink.webserver.core.enumeration.HttpStatus.SERVER_ERROR;

/**
 * 响应协议
 *
 * @author chenjian
 */
@Slf4j
public class Response {
    private OutputStream os;
    /**
     * 文本正文
     */
    private StringBuilder content;
    /**
     * 协议头（状态行与请求头 回车）信息
     */
    private StringBuilder headInfo;
    /**
     * 内容长度
     */
    private int len;

    /**
     * 响应体
     */
    private byte[] body;

    private final static String BLANK = " ";
    private final static String DEFAULT_CONTENTTYPE = "text/html;charset=UTF-8";

    private Response() {
        content = new StringBuilder();
        headInfo = new StringBuilder();
        len = 0;
    }

    public Response(Socket client) {
        this();
        try {
            os = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            headInfo = null;
        }
    }

    public Response(OutputStream os) {
        this();
        this.os = os;
    }

    /**
     * 动态添加文本内容
     *
     * @param info
     * @return
     */
    public Response print(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    /**
     * 一次性添加所有文本内容
     *
     * @param info
     * @return
     */
    public Response println(String info) {
        content.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;
        return this;
    }


    /**
     * 通过流来构建响应体
     *
     * @param data
     */
    public void createbody(byte[] data) {
        this.body = data;
        len += data.length;
    }

    /**
     * 推送响应信息
     *
     * @throws IOException
     */
    public void write() throws IOException {
        if (null == headInfo) {
            setHeader(SERVER_ERROR);
        }
        if (null != headInfo && headInfo.length() <= 0) {
            setHeader(OK);
        }
        byte[] headInfoByte = headInfo.toString().getBytes();
        if (body == null) {
            log.info("使用print或者pringln构建响应体");
            body = content.toString().getBytes();
        }
        try {
            os.write(headInfoByte);
            os.write(body);
            os.flush();
        } catch (IOException e) {
            log.error("响应异常", e);
        } finally {
            os.close();
        }
    }

    public void setHeader(HttpStatus status, String contentType) {
        createHeadInfo(status, contentType);
    }

    public void setHeader(HttpStatus status) {
        createHeadInfo(status, DEFAULT_CONTENTTYPE);
    }

    /**
     * 构建头信息
     *
     * @param status
     */
    private void createHeadInfo(HttpStatus status, String contentType) {
        //1、响应行: HTTP/1.1
        headInfo.append("HTTP/1.1").append(BLANK);
        headInfo.append(status.getCode()).append(BLANK);
        headInfo.append(status).append(CRLF);
        //2、响应头(最后一行存在空行):
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Server:").append("chenjianlink Server/0.0.1;charset=utf-8").append(CRLF);
        //设置响应类型
        headInfo.append("Content-type:").append(contentType).append(CRLF);
        headInfo.append("Content-length:").append(len).append(CRLF);
        headInfo.append(CRLF);
    }

}

package cn.chenjianlink.webserver.core.response;

import cn.chenjianlink.webserver.core.enumeration.HttpStatus;
import com.sun.istack.internal.Nullable;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
    private BufferedWriter bw;
    /**
     * 正文
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

    private final String BLANK = " ";
    private final String CRLF = "\r\n";
    private final int DEFAULT_STATUS = 200;
    /**
     * 响应编码
     */
    @Setter
    private Integer status;

    private Response() {
        content = new StringBuilder();
        headInfo = new StringBuilder();
        len = 0;
    }

    public Response(Socket client) {
        this();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            headInfo = null;
        }
    }

    public Response(OutputStream os) {
        this();
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    /**
     * 动态添加内容
     *
     * @param info
     * @return
     */
    public Response print(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    public Response println(String info) {
        content.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;
        return this;
    }


    /**
     * 推送响应信息
     *
     * @throws IOException
     */
    public void write() throws IOException {
        if (null == headInfo) {
            status = 500;
        }
        if (status == null) {
            createHeadInfo(DEFAULT_STATUS);
        }else {
            createHeadInfo(status);
        }
        bw.append(headInfo);
        bw.append(content);
        bw.flush();
    }

    /**
     * 构建头信息
     *
     * @param status
     */
    private void createHeadInfo(int status) {
        //1、响应行: HTTP/1.1
        headInfo.append("HTTP/1.1").append(BLANK);
        headInfo.append(status).append(BLANK);
        headInfo.append("OK").append(CRLF);
        //2、响应头(最后一行存在空行):
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Server:").append("chenjianlink Server/0.0.1;charset=utf-8").append(CRLF);
        //设置响应类型
        headInfo.append("Content-type:").append(" ").append(CRLF);
        headInfo.append("Content-length:").append(len).append(CRLF);
        headInfo.append(CRLF);
    }

}

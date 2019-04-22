package cn.chenjianlink.webserver.core.request;

import cn.chenjianlink.webserver.core.enumeration.HttpMethod;
import cn.chenjianlink.webserver.core.exception.IllegalRequestException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装请求协议: 封装请求参数为Map
 */
@Slf4j
public class Request {
    /**
     * 协议信息
     */
    private String requestInfo;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求url
     */
    private String url;
    /**
     * 请求参数
     */
    private String queryStr;
    /**
     * 存储参数
     */
    private Map<String, List<String>> parameterMap;
    private final String CRLF = "\r\n";

    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }

    public Request(InputStream is) {
        parameterMap = new HashMap<String, List<String>>();
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            byte[] data = new byte[bufferedInputStream.available()];
            int len = bufferedInputStream.read(data);
            if (len<=0){
                throw new IllegalRequestException("非法请求");
            }
            this.requestInfo = new String(data, 0, len);
        } catch (IOException e) {
            log.error("请求异常", e);
            e.printStackTrace();
            return;
        }
        //分解字符串
        parseRequestInfo();
    }

    /**
     * 分解字符串
     */
    private void parseRequestInfo() {
        log.info("开始分解请求");
        this.method = this.requestInfo.substring(0, this.requestInfo.indexOf("/")).toUpperCase();
        this.method = this.method.trim();
        //1)、获取/的位置
        int startIdx = this.requestInfo.indexOf("/") + 1;
        //2)、获取 HTTP/的位置
        int endIdx = this.requestInfo.indexOf("HTTP/");
        //3)、分割字符串
        this.url = this.requestInfo.substring(startIdx, endIdx).trim();
        //4)、获取？的位置
        int queryIdx = this.url.indexOf("?");
        if (queryIdx >= 0) {
            //表示存在请求参数
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0];
            queryStr = urlArray[1];
        }

        log.info("获取请求参数");
        if (method.equals(HttpMethod.POST.toString())) {
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            if (null == queryStr) {
                queryStr = qStr;
            } else {
                queryStr += "&" + qStr;
            }
        }
        queryStr = null == queryStr ? "无参数" : queryStr;
        log.info("请求方式：" + method + "-->" + "请求地址:" + url + " -->" + "请求参数:" + queryStr);
        convertMap();
    }

    /**
     * 处理请求参数为Map
     */
    private void convertMap() {
        //1、分割字符串 &
        String[] keyValues = this.queryStr.split("&");
        for (String queryStr : keyValues) {
            //2、再次分割字符串  =
            String[] kv = queryStr.split("=");
            kv = Arrays.copyOf(kv, 2);
            //获取key和value
            String key = kv[0];
            String value = kv[1] == null ? null : decode(kv[1], "utf-8");
            //存储到map中
            if (!parameterMap.containsKey(key)) {
                //第一次
                parameterMap.put(key, new ArrayList<String>());
            }
            parameterMap.get(key).add(value);
        }
    }

    /**
     * 处理中文
     *
     * @return
     */
    private String decode(String value, String enc) {
        try {
            return URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            log.error("请求编码异常", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过name获取对应的多个值
     *
     * @param key
     * @return
     */
    public String[] getParameterValues(String key) {
        List<String> values = this.parameterMap.get(key);
        if (null == values || values.size() < 1) {
            return null;
        }
        return values.toArray(new String[0]);
    }

    /**
     * 通过name获取对应的一个值
     *
     * @param key
     * @return
     */
    public String getParameter(String key) {
        String[] values = getParameterValues(key);
        return values == null ? null : values[0];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryStr() {
        return queryStr;
    }


}

package cn.chenjianlink.webserver.core.utils;

import cn.chenjianlink.webserver.core.comment.GeneralResources;
import cn.chenjianlink.webserver.core.context.WebApp;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * IO流工具类
 *
 * @author chenjian
 */
@Slf4j
public class IOUtils {

    /**
     * 读取页面并返回页面
     */
    public static String inputStreamToSring(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String tempOneLine;
                while ((tempOneLine = bf.readLine()) != null) {
                    sb.append(tempOneLine);
                }
                return sb.toString();
            } catch (IOException e) {
                log.error("转换错误", e);
                e.printStackTrace();
            } finally {
                inputStream.close();
            }
        }
        return "";
    }

    /**
     * 输入流在转byte数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static boolean isFileExist(String fileName) {
        if (fileName != null && !fileName.equals(GeneralResources.EMPTY)) {
            try {
                File file = new File(WebApp.class.getClassLoader().getResource(fileName).toURI());
                if (file.exists()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

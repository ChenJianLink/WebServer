package cn.chenjianlink.webserver.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件
 */
@Slf4j
public class PropertiesUtils {
    private static final String PORT = "webserver.port";
    private static final Map<String,String> PROOERTIES = new HashMap<>();

    static {
        try {
            log.info("开始解析配置文件");
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("webserverenvironment.properties");
            Properties prop = new Properties();
            prop.load(inputStream);
            String port = prop.getProperty(PORT);
            PROOERTIES.put(PORT, port);
            log.info("配置文件解析完成");
        } catch (IOException e) {
            log.error("配置文件解析错误", e);
            e.printStackTrace();
        }
    }

    /**
     * 获取端口号
     *
     * @return
     */
    public static int getPort() {
        String port = PROOERTIES.get(PORT);
        return Integer.parseInt(port);
    }
}

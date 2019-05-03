package cn.chenjianlink.webserver.core.utils;

import cn.chenjianlink.webserver.core.comment.GeneralResources;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件
 *
 * @author chenjian
 */
@Slf4j
public class PropertiesUtils {
    private static final String PORT = "webserver.port";
    private static final String CONNECTION_NUMBER = "webserver.connections";
    private static final Map<String, String> PROOERTIES = new HashMap<>();

    static {
        try {
            log.info("开始解析配置文件");
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("webserverenvironment.properties");
            Properties prop = new Properties();
            prop.load(inputStream);
            String port = prop.getProperty(PORT);
            String connections = prop.getProperty(CONNECTION_NUMBER);
            PROOERTIES.put(PORT, port);
            PROOERTIES.put(CONNECTION_NUMBER, connections);
            log.info("配置文件解析完成");
        } catch (IOException e) {
            log.error("配置文件解析错误", e);
            e.printStackTrace();
        }
    }

    /**
     * 获取端口号
     *
     * @return 返回端口号
     */
    public static Integer getPort() {
        String port = PROOERTIES.get(PORT);
        if (port != null && !port.equals(GeneralResources.EMPTY)) {
            return Integer.parseInt(port);
        }
        return null;
    }

    /**
     * 获取连接数
     *
     * @return 返回连接数
     */
    public static Integer getConnections() {
        String connections = PROOERTIES.get(CONNECTION_NUMBER);
        if (connections != null && !connections.equals(GeneralResources.EMPTY)) {
            return Integer.parseInt(connections);
        }
        return null;
    }


}

package cn.chenjianlink.webserver.core.exception;

/**
 * 服务器启动异常
 *
 * @author chenjian
 */
public class ServerStartException extends Exception {
    public ServerStartException() {
        super();
    }

    public ServerStartException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

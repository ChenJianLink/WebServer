package cn.chenjianlink.webserver.core.exception;

/**
 * 无静态资源异常
 *
 * @author chenjian
 */
public class StaticResourceNotFoundException extends Exception {
    public StaticResourceNotFoundException() {
        super();
    }

    public StaticResourceNotFoundException(String message) {
        super(message);
    }

    public StaticResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

package cn.chenjianlink.webserver.core.exception;

/**
 * Servlet未找到异常
 * @author chenjian
 */
public class ServletNotFoundException extends Exception {

    public ServletNotFoundException() {
        super();
    }

    public ServletNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

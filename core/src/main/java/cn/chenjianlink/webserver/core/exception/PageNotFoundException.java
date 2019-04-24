package cn.chenjianlink.webserver.core.exception;

/**
 * 页面找不到异常
 *
 * @author chenjian
 */
public class PageNotFoundException extends Exception {
    public PageNotFoundException() {
        super();
    }

    public PageNotFoundException(String message) {
        super(message);
    }

    public PageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

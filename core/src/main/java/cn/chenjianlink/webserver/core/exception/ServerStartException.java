package cn.chenjianlink.webserver.core.exception;

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

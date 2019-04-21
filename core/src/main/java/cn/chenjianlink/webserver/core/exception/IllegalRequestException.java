package cn.chenjianlink.webserver.core.exception;

import java.io.IOException;

public class IllegalRequestException extends IOException {
    public IllegalRequestException() {
        super();
    }

    public IllegalRequestException(String message) {
        super(message);
    }

    public IllegalRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

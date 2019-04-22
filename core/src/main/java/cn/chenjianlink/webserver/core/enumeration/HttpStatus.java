package cn.chenjianlink.webserver.core.enumeration;


/**
 * 服务器响应状态
 * @author chenjian
 */
public enum HttpStatus {
    //请求成功
    OK(200),
    //404
    NOT_FOUND(404),
    //500
    SERVER_ERROR(500);
    private int code;

    HttpStatus(int code) {
        this.code = code;
    }
    public int getCode(){
        return code;
    }
}

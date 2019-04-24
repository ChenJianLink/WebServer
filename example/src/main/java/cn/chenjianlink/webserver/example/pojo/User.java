package cn.chenjianlink.webserver.example.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户表
 * @author chenjian
 */
@Getter
@Setter
public class User {
    private String username;
    private String password;
    /**
     * 个性签名
     */
    private String messages;

}

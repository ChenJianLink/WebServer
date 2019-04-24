package cn.chenjianlink.webserver.example.test;

import cn.chenjianlink.webserver.core.utils.MimeTypeUtil;
import org.junit.Test;

/**
 * MimeTypeUtil测试类
 */
public class MimeUtilTest {
    @Test
    public void checkMimeType() {
        String fileType = MimeTypeUtil.findFileType("web.xml");
        String fileType1 = MimeTypeUtil.findFileType("images/favicon.ico");
        String fileType2 = MimeTypeUtil.findFileType("/home/chenjian/20170507010814.jpg");
        System.out.println(fileType);
        System.out.println(fileType1);
        System.out.println(fileType2);
    }
}

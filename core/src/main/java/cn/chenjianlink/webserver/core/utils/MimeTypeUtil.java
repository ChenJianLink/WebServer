package cn.chenjianlink.webserver.core.utils;

import eu.medsea.mimeutil.MimeUtil;

import java.util.Collection;

/**
 * 响应类型转换器
 *
 * @author chenjian
 */
public class MimeTypeUtil {

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    public static String findFileType(String fileName) {
        Collection mimeTypes = MimeUtil.getMimeTypes(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
        return mimeTypes.toString();
    }
}

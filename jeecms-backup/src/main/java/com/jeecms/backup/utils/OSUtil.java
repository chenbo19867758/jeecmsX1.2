package com.jeecms.backup.utils;

import java.nio.charset.Charset;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/1 15:02
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class OSUtil {

    public static final int PLATFORM_WINDOWS = 1;
    public static final int PLATFORM_LINUX = 2;
    public static final int PLATFORM_MAC = 3;
    public static final int PLATFORM_UNKNOWN = -1;

    private static final String OS_WINDOWS_START = "Windows";
    private static final String OS_LINUX_START = "Linux";
    private static final String OS_MAC_START = "Mac";

    public static int currentPlatform() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith(OS_WINDOWS_START)) {
            return PLATFORM_WINDOWS;
        } else if (osName.startsWith(OS_LINUX_START)) {
            return PLATFORM_LINUX;
        } else if (osName.startsWith(OS_MAC_START)) {
            return PLATFORM_MAC;
        }
        return PLATFORM_UNKNOWN;
    }

    public static String platformCharsetName() {
        String charsetName = "UTF-8";
        if (OSUtil.currentPlatform() == OSUtil.PLATFORM_WINDOWS) {
            charsetName = "GBK";
        }
        return charsetName;
    }

    public static Charset platformCharset() {
        return Charset.forName(platformCharsetName());
    }
}

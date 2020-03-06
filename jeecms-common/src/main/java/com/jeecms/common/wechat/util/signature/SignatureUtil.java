package com.jeecms.common.wechat.util.signature;


import java.util.Arrays;

import com.jeecms.common.util.StrUtils;

/**
 * 与微信服务交互相关的安全校验.
 *
 * @author vioao
 */
public class SignatureUtil {

    /**
     * 验证签名.
     *
     * @param originalSignature 正确的签名
     * @param params            待验证的参数
     * @return {boolean}
     */
    public static boolean check(String originalSignature, String... params) {
        if (!StrUtils.isAnyEmpty(params)) {
            String calculated = signature(params);
            return calculated.equals(originalSignature);
        }
        return false;
    }

    /**
     * 对参数排序后进行SHA1加密得到16进制字符串.
     *
     * @param params 加密的参数
     * @return SHA1加密后的16进制字符串
     */
    public static String signature(String... params) {
        Arrays.sort(params);

        StringBuilder src = new StringBuilder();
        for (String param : params) {
            src.append(param);
        }

        try {
            return DigestUtil.sha1Hex(src.toString());
        } catch (Exception e) {
            throw new RuntimeException("SHA1加密生成签名失败");
        }
    }
}

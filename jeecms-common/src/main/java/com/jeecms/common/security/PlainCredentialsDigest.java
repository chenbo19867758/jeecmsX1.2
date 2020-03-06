package com.jeecms.common.security;

import org.apache.commons.lang3.StringUtils;

/**
 * 明文证书
 * @author: tom
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class PlainCredentialsDigest {
	/**
	 * 加密密码
	 * @Title: encodePassword  
	 * @param rawPass 明文
	 * @param salt 混淆码
	 * @return: String
	 */
	public String encodePassword(String rawPass, String salt) {
		if (StringUtils.isNotBlank(rawPass)) {
			return rawPass;
		} else {
			return null;
		}
	}

	/**
	 * 密码是否正确
	 * @Title: isPasswordValid  
	 * @param encPass 密文
	 * @param rawPass 明文
	 * @param salt 混淆码
	 * @return: boolean
	 */
	public boolean isPasswordValid(String encPass, String rawPass, String salt) {
		if (StringUtils.isBlank(encPass) && StringUtils.isBlank(rawPass)) {
			return true;
		}
		return StringUtils.equals(encPass, rawPass);
	}
}

package com.jeecms.common.security;

/**
 * 证书加密
 * @author: tom
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.  Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CredentialsDigest {
	/**
	 * 散列生成摘要
	 * @Title: digest  
	 * @param plainCredentials 密码明文
	 * @param salt 混淆码数组
	 * @return: String
	 */
	public String digest(String plainCredentials, byte[] salt);

	/**
	 * 密码是否匹配
	 * @Title: matches  
	 * @param credentials  加密后摘要 digest方法得到
	 * @param plainCredentials 密码原文
	 * @param salt 混淆码数组
	 * @return: boolean
	 */
	public boolean matches(String credentials, String plainCredentials,
			byte[] salt);
}

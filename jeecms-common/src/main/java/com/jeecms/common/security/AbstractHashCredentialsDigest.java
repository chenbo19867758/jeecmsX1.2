package com.jeecms.common.security;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.util.Encodes;


/**
 * Hash证书加密
 * @author: tom
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public abstract class AbstractHashCredentialsDigest implements CredentialsDigest {
	public static final int HASH_INTERATIONS = 1024;

	@Override
	public String digest(String plainCredentials, byte[] salt) {
		if (StringUtils.isBlank(plainCredentials)) {
			return null;
		}
		byte[] hashPassword = digest(Cryptos.utf8encode(plainCredentials), salt);
		return Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 需要实现加密转换
	 * @Title: digest  
	 * @param input 输入字节数组
	 * @param salt 混淆字节数组
	 * @return: byte[]
	 */
	protected abstract byte[] digest(byte[] input, byte[] salt);

	@Override
	public boolean matches(String credentials, String plainCredentials,
			byte[] salt) {
		if (StringUtils.isBlank(credentials)
				&& StringUtils.isBlank(plainCredentials)) {
			return true;
		}
		return StringUtils.equals(credentials, digest(plainCredentials, salt));
	}

}

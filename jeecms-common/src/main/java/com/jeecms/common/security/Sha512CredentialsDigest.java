package com.jeecms.common.security;

/**
 * SHA512证书加密
 * @author: tom
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Sha512CredentialsDigest extends AbstractHashCredentialsDigest implements
		CredentialsDigest {
	@Override
	protected byte[] digest(byte[] input, byte[] salt) {
		return Digests.sha512(input, salt, HASH_INTERATIONS);
	}
}
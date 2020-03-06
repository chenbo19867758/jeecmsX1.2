package com.jeecms.common.security;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 支持HMAC-SHA1消息签名 及 DES/AES对称加密的工具类. 支持Hex与Base64两种编码方式.
 * 
 * @author: tom
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Cryptos {
	private static final String HMACSHA1 = "HmacSHA1";
	// RFC2401
	/**
	 * sha1 长度
	 */
	private static final int DEFAULT_HMACSHA1_KEYSIZE = 160;

	// -- HMAC-SHA1 funciton --//
	/**
	 * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
	 * 
	 * @param input
	 *            原始输入字符数组
	 * @param key
	 *            HMAC-SHA1密钥
	 */
	public static byte[] hmacSha1(byte[] input, byte[] key) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, HMACSHA1);
			Mac mac = Mac.getInstance(HMACSHA1);
			mac.init(secretKey);
			return mac.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 校验HMAC-SHA1签名是否正确.
	 * 
	 * @param expected
	 *            已存在的签名
	 * @param input
	 *            原始输入字符串
	 * @param key
	 *            密钥
	 */
	public static boolean isMacValid(byte[] expected, byte[] input, byte[] key) {
		byte[] actual = hmacSha1(input, key);
		return Arrays.equals(expected, actual);
	}

	/**
	 * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节). HMAC-SHA1算法对密钥无特殊要求,
	 * RFC2401建议最少长度为160位(20字节).
	 */
	public static byte[] generateHmacSha1Key() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
			keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}


	private static final Charset CHARSET = Charset.forName("UTF-8");

	/**
	 * Get the bytes of the String in UTF-8 encoded form.
	 */
	public static byte[] utf8encode(CharSequence string) {
		try {
			ByteBuffer bytes = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
			byte[] bytesCopy = new byte[bytes.limit()];
			System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());

			return bytesCopy;
		} catch (CharacterCodingException e) {
			throw new IllegalArgumentException("Encoding failed", e);
		}
	}

	/**
	 * Decode the bytes in UTF-8 form into a String.
	 */
	public static String utf8decode(byte[] bytes) {
		try {
			return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
		} catch (CharacterCodingException e) {
			throw new IllegalArgumentException("Decoding failed", e);
		}
	}
	
}
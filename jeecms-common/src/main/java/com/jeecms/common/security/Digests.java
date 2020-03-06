package com.jeecms.common.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.apache.commons.lang3.Validate;

import com.jeecms.common.util.Encodes;

/**
 * 支持SHA-1/MD5消息摘要的工具类. 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * 
 * @author: tom
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Digests {

	private static final String SHA1 = "SHA-1";
	private static final String SHA256 = "sha-256";
	private static final String SHA512 = "sha-512";
	private static final String MD5 = "MD5";
	public static final Integer SALT_SIZE = 8;

	private static SecureRandom random = new SecureRandom();

	/**
	 * 对输入字符串进行sha1散列.
	 */
	public static byte[] sha1(byte[] input) {
		return digest(input, SHA1, null, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt) {
		return digest(input, SHA1, salt, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA1, salt, iterations);
	}

	/**
	 * 对文件进行sha1散列.
	 */
	public static byte[] sha1(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	/**
	 * 对输入字符串进行sha256散列.
	 */
	public static byte[] sha256(byte[] input) {
		return digest(input, SHA256, null, 1);
	}

	public static byte[] sha256(byte[] input, byte[] salt) {
		return digest(input, SHA256, salt, 1);
	}

	public static byte[] sha256(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA256, salt, iterations);
	}

	/**
	 * 对输入字符串进行sha512散列.
	 */
	public static byte[] sha512(byte[] input) {
		return digest(input, SHA512, null, 1);
	}

	public static byte[] sha512(byte[] input, byte[] salt) {
		return digest(input, SHA512, salt, 1);
	}

	public static byte[] sha512(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA512, salt, iterations);
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 */
	private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加密生成摘要
	 * @Title: digest  
	 * @param input 输入流
	 * @param algorithm 算法
	 * @throws IOException    IOException  
	 * @return: byte[]
	 */
	private static byte[] digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 8 * 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return messageDigest.digest();
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 生成随机的Byte[]作为salt.
	 * 
	 * @param numBytes
	 *            byte数组的大小
	 */
	public static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}

	public static byte[] generateSaltFix() {
		return generateSalt(SALT_SIZE);
	}

	public static String getSaltStr(byte[] saltBytes) {
		String salt = Encodes.encodeHex(saltBytes);
		return salt;
	}

	/**
	 * 对文件进行md5散列.
	 */
	public static byte[] md5(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	

}

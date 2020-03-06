package com.jeecms.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.serialization.ValidatingObjectInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流工具类
 * 
 * @author: tom
 * @date: 2019年1月9日 上午10:58:14
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class StreamUtil {
	static Logger logger = LoggerFactory.getLogger(StreamUtil.class);

	/**
	 * 对象转byte
	 * 
	 * @param obj
	 *            对象
	 * @return
	 */
	public static byte[] objectToByte(Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return bytes;
	}

	/**
	 * byte转对象
	 * 
	 * @param bytes
	 *            需要序列化的字节数组
	 * @return 对象
	 */
	public static Object byteToObject(byte[] bytes) {
		Object obj = new Object();
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);

			// ObjectInputStream oi = new ObjectInputStream(bi);

			// Use ValidatingObjectInputStream instead of InputStream
			ValidatingObjectInputStream oi = new ValidatingObjectInputStream(bi);
			// 只允许反序列化SerialObject class
			// ois.accept(SerialObject.class);

			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return obj;
	}

}

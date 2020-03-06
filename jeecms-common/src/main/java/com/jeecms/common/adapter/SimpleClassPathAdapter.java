package com.jeecms.common.adapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hankcs.hanlp.corpus.io.IIOAdapter;

/**
 * hanlp IO适配器
 * @author: tom
 * @date: 2018年12月5日 下午4:53:58
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SimpleClassPathAdapter implements IIOAdapter {
	private Logger logger = LoggerFactory.getLogger(SimpleClassPathAdapter.class);

	/**
	 * 打开一个文件以供读取
	 *
	 * @param path
	 *            文件路径
	 * @return 一个输入流
	 * @throws IOException
	 *             任何可能的IO异常
	 */
	@Override
	public InputStream open(String path) throws IOException {
		return SimpleClassPathAdapter.class.getResourceAsStream(path);
	}

	/**
	 * 创建一个新文件以供输出
	 *
	 * @param path
	 *            文件路径
	 * @return 一个输出流
	 * @throws IOException
	 *             任何可能的IO异常
	 */
	@Override
	public OutputStream create(String path) throws IOException {
		String filepath = this.getClass().getClassLoader().getResource(path).getFile();
		try {
			/** 中文文件夹需要解码 */
			filepath = URLDecoder.decode(filepath, "utf8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return new FileOutputStream(filepath);
	}
}

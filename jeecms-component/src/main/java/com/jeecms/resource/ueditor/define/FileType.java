package com.jeecms.resource.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FileType {

	public static final String JPG = "JPG";

	private static final Map<String, String> TYPES = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{

			put(FileType.JPG, ".jpg");

		}
	};

	public static String getSuffix(String key) {
		return FileType.TYPES.get(key);
	}

	/**
	 * 根据给定的文件名,获取其后缀信息
	 * 
	 * @param filename
	 * @return
	 */
	public static String getSuffixByFilename(String filename) {

		return filename.substring(filename.lastIndexOf(".")).toLowerCase();

	}

}

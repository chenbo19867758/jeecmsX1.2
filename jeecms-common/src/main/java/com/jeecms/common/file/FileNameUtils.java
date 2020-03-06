package com.jeecms.common.file;

import java.util.Calendar;

import org.apache.commons.lang.RandomStringUtils;

import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.Num62;

/**
 *  文件名生成帮助类
 * @author: tom
 * @date:   2018年12月25日 上午9:43:56     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FileNameUtils {

	/**
	 * 生成当前年月格式的文件路径
	 * yyyyMM 200806
	 * 
	 * @return
	 */
	public static String genPathName() {
		return MyDateUtils.formatDate(Calendar.getInstance().getTime(), MyDateUtils.COM_YM_PATTERN);
	}

	/**
	 * 生产以当前日、时间开头加4位随机数的文件名  ddHHmmss 03102230
	 * @return 10位长度文件名
	 */
	public static String genFileName() {
		return MyDateUtils.formatDate(Calendar.getInstance().getTime(), 
				MyDateUtils.COM_D_H_M_S_PATTERN) + RandomStringUtils.random(4, Num62.N36_CHARS);
	}

	/**
	 * 生产以当前时间开头加4位随机数的文件名
	 * 
	 * @param ext
	 *            文件名后缀，不带'.'
	 * @return 10位长度文件名+文件后缀
	 */
	public static String genFileName(String ext) {
		return genFileName() + "." + ext;
	}

	/**
	 * 依据文件名获取文件后缀
	 * @Title: getFileSufix  
	 * @param fileName 文件名
	 * @return: String 后缀格式
	 */
	public static String getFileSufix(String fileName) {
		boolean normalImg = false;
		for (String imgExt : ImageUtils.IMAGE_EXT) {
			if (fileName.endsWith(imgExt)) {
				normalImg = true;
			}
		}
		String suffix = "";
		if (normalImg) {
			int splitIndex = fileName.lastIndexOf(".");
			suffix = fileName.substring(splitIndex + 1);
		} else {
			suffix = ImageUtils.IMAGE_EXT[0];
		}
		return suffix;
	}
}

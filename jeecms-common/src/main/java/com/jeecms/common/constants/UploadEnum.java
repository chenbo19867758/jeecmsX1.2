/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.constants;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.ueditor.Utils;

/**
 * 上传相关枚举
 * 
 * @author: tom
 * @date: 2019年5月6日 下午6:52:32
 */
public class UploadEnum {

	/**
	 * 上传文件存储服务器
	 * 
	 * @author: tom
	 * @date: 2019年5月6日 下午6:27:11
	 */
	public enum UploadServerType {
		/**
		 * 本地服务器
		 */
		local("local"),
		/**
		 * FTP
		 */
		ftp("ftp"),
		/**
		 * OSS
		 */
		oss("oss"),;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private UploadServerType(String name) {
			this.name = name;
		}

		/**
		 * 是否正确的上传文件存储方式配置
		 * 
		 * @Title: isCorrectServerTypeStr
		 * @param type
		 *            配置的存储方式字符串
		 * @return: boolean 正确返回true
		 */
		public static boolean isCorrectServerTypeStr(String type) {
			if (StringUtils.isBlank(type)) {
				return false;
			} else if (local.name().equals(type)) {
				return true;
			} else if (ftp.name().equals(type)) {
				return true;
			} else if (oss.name().equals(type)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 水印设置
	 * 
	 * @author: tom
	 * @date: 2019年5月7日 下午5:55:37
	 */
	public enum WaterMarkSet {

		/**
		 * 3不使用水印
		 */
		no(3),
		/**
		 * 2使用文字水印
		 */
		txt(2),
		/**
		 * 1使用图片水印
		 */
		img(1),;
		Integer value;

		private WaterMarkSet(Integer value) {
			this.value = value;
		}

		/**
		 * 获取水印值
		 * 
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}

		/**
		 * 设置水印值
		 * 
		 * @param value
		 *            the value to set
		 */
		public void setValue(Integer value) {
			this.value = value;
		}
	}

	/**
	 * 水印位置枚举
	 * 
	 * @author: tom
	 * @date: 2019年5月7日 下午6:03:22
	 */
	public enum WaterMarkPosition {

		/**
		 * 1左上 2上 3右上 4左 5中 6右 7左下 8下 9右下
		 */
		leftUpper(1),
		/**
		 * 2上
		 */
		upper(2),
		/**
		 * 3右上
		 */
		rightUpper(3),
		/**
		 * 4左
		 */
		left(4),
		/**
		 * 5中
		 */
		center(5),
		/**
		 * 6右
		 */
		right(6),
		/**
		 * 左下
		 */
		leftLower(7),
		/**
		 * 8下
		 */
		lower(8),
		/**
		 * 9右下
		 */
		rightLower(9),;
		Integer value;

		private WaterMarkPosition(Integer value) {
			this.value = value;
		}

		/**
		 * 获取水印位置
		 * 
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}

		/**
		 * 设置水印位置
		 * 
		 * @param value
		 *            the value to set
		 */
		public void setValue(Integer value) {
			this.value = value;
		}
	}
	
	/**
	 * 上传限制类型
	 * @author: tom
	 * @date:   2019年10月23日 下午5:24:57
	 */
	public enum UploadLimitType {
		/**
		 * 不限制
		 */
		no("1"),
		/**
		 * 白名单
		 */
		white("2"),
		/**
		 * 黑名单
		 */
		black("3"),;
		
		private static Map<String, UploadLimitType> types = new HashMap<String, UploadLimitType>(3);
		static {
			types.put(no.getName(), no);
			types.put(white.getName(), white);
			types.put(black.getName(), black);
		}
		
		private String limitVal;

		public String getName() {
			return limitVal;
		}

		public void setName(String name) {
			this.limitVal = name;
		}

		private UploadLimitType(String name) {
			this.limitVal = name;
		}
		
		public static UploadLimitType getValueOf(final String name) {
			if (Utils.isEmpty(name)) {
				throw new NullPointerException("Name is null or empty");
			}
			UploadLimitType rt = types.get(name);
			if (rt == null) {
				throw new IllegalArgumentException("No UploadLimitType const " + name);
			}
			return rt;
		}

		/**
		 * 是否正确的上传限制类型
		 * 
		 * @Title: isCorrectServerTypeStr
		 * @param type  上传限制类型字符串
		 * @return: boolean 正确返回true
		 */
		public static boolean isCorrectServerTypeStr(String type) {
			if (StringUtils.isBlank(type)) {
				return false;
			} else if (no.name().equals(type)) {
				return true;
			} else if (white.name().equals(type)) {
				return true;
			} else if (black.name().equals(type)) {
				return true;
			}
			return false;
		}
	}

}

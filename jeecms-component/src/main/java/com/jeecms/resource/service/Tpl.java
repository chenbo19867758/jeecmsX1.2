package com.jeecms.resource.service;

import com.jeecms.common.exception.GlobalException;

import java.util.Date;

/**
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface Tpl {
	/**
	 * 获得模板完整名称，是文件的唯一标识。
	 *
	 * @return
	 */
	String getName();

	/**
	 * 获得路径，不包含文件名的路径。
	 *
	 * @return
	 */
	String getRoot();

	/**
	 * 获得模板名称，不包含路径的文件名。
	 *
	 * @return
	 */
	String getFilename();

	/**
	 * 获得模板内容
	 *
	 * @return
	 */
	String getSource() throws GlobalException;

	/**
	 * 获得最后修改时间（毫秒）
	 *
	 * @return
	 */
	long getLastModified();

	/**
	 * 获得最后修改时间（日期）
	 *
	 * @return
	 */
	Date getLastModifiedDate();

	/**
	 * 获得文件大小，单位bytes
	 *
	 * @return
	 */
	long getLength();

	/**
	 * 获得文件大小，单位K bytes
	 *
	 * @return
	 */
	int getSize();

	/**
	 *  转换文件大小
	 * @return
	 */
	String getSizeUnit();

	/**
	 * 是否目录
	 *
	 * @return
	 */
	boolean isDirectory();

	/**
	 * 模板对象
	 *
	 * @return
	 */
	Integer[] models();
}

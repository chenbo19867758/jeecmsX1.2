/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.ContentSource;

/**
 * 来源管理service层
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年5月6日 上午11:45:04
 */
public interface ContentSourceService extends IBaseService<ContentSource, Integer> {

	/**
	 * 添加来源信息
	 * 
	 * @Title: saveSysSourceInfo
	 * @param sysSource	来源对象
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	ResponseInfo saveSysSourceInfo(ContentSource sysSource) throws GlobalException;

	/**
	 * 修改来源信息
	 * 
	 * @Title: updateSysSourceInfo
	 * @param sysSource	来源对象
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	ResponseInfo updateSysSourceInfo(ContentSource sysSource) throws GlobalException;

	/**
	 * 通过来源名称查询来源信息
	 * 
	 * @Title: findByTypeName
	 * @param sourceName	来源名称
	 * @throws GlobalException 异常
	 * @return: ContentType
	 */
	ContentSource findBySourceName(String sourceName) throws GlobalException;

	/**
	 * 查询默认来源
	 * 
	 * @Title: defaultSource
	 * @throws GlobalException 异常
	 * @return: ContentSource
	 */
	ContentSource defaultSource() throws GlobalException;
}

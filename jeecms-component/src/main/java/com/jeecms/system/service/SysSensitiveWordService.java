/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysSensitiveWord;
import com.jeecms.system.domain.dto.SensitiveWordDto;

import java.util.List;

/**
 * 敏感词Service接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-29
 */
public interface SysSensitiveWordService extends IBaseService<SysSensitiveWord, Integer> {

	/**
	 * 添加敏感词（分割输入的敏感词添加多个）
	 *
	 * @param dto      新增敏感词dto
	 * @param userName 用户名
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	ResponseInfo saveBatch(SensitiveWordDto dto, String userName) throws GlobalException;

	/**
	 * 批量添加
	 *
	 * @param list         敏感词数组
	 * @param dealWithType 重复数据处理方式
	 * @param userName     用户名
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	ResponseInfo saveAll(List<SysSensitiveWord> list, Integer dealWithType, String userName) throws GlobalException;

	/**
	 * 校验敏感词是否存在
	 *
	 * @param sensitiveWord 敏感词
	 * @param id            敏感词id
	 * @return true 不存在或为空， false 存在
	 */
	boolean checkBySensitiveWord(String sensitiveWord, Integer id);
}

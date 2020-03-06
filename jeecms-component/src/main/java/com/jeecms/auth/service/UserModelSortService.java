/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.auth.service;

import java.util.List;

import com.jeecms.auth.domain.UserModelSort;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModel;

/**
 * 用户排序
* @author ljw
* @version 1.0
* @date 2019-12-13
*/
public interface UserModelSortService extends IBaseService<UserModelSort, Integer> {
	
	/**
	 * 根据用户ID获取最近模型排序
	* @Title: getList 
	* @param userId 用户ID
	* @throws GlobalException 异常
	 */
	List<UserModelSort> getSortList(Integer userId) throws GlobalException;

	/**
	 * 模型排序
	* @Title: sort 
	* @param models 模型列表
	* @param channelId 栏目ID
	* @param userId 用户ID
	* @throws GlobalException 异常
	 */
	List<CmsModel> sort(List<CmsModel> models, Integer channelId, Integer userId) throws GlobalException;
}

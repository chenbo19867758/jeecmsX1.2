/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.auth.dao.ext;

import java.util.List;

import com.jeecms.auth.domain.UserModelSort;

/**   
 * Dao扩展
 * @author: ljw
 * @date:   2019年12月13日 下午4:45:31     
 */
public interface UserModelSortDaoExt {

	/**
	 * 根据用户ID获取最近的模型使用数据
	* @Title: getSort 
	* @param userId 用户ID
	* @return
	 */
	List<UserModelSort> getSortList(Integer userId);
}

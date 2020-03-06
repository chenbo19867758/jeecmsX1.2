/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service;

import com.jeecms.member.domain.UserTotal;
import com.jeecms.common.base.service.IBaseService;

/**
 * Service
* @author ljw
* @version 1.0
* @date 2019-09-23
*/
public interface UserTotalService extends IBaseService<UserTotal, Integer> {

	/**
	 * 根据UserId查询记录
	* @Title: findByUserId 
	* @param userId 用户ID
	* @return
	 */
	UserTotal findByUserId(Integer userId);
}

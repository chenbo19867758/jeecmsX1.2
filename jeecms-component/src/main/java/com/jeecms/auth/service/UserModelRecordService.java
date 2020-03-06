/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.auth.service;

import java.util.Date;
import java.util.List;

import com.jeecms.auth.domain.UserModelRecord;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 用户模型记录Service
* @author ljw
* @version 1.0
* @date 2019-12-13
*/
public interface UserModelRecordService extends IBaseService<UserModelRecord, Integer> {

	/**
	 * 统计
	* @Title: getList  
	* @param startDate 开始时间
	* @param endDate 结束时间
	* @throws GlobalException 异常
	 */
	void collect(Date startDate, Date endDate) throws GlobalException;
}

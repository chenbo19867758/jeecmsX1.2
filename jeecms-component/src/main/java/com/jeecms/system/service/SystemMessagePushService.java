package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SystemMessagePush;

/**
 * @author chenming
 * @version 1.0
 * @date 2019-01-24
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface SystemMessagePushService extends IBaseService<SystemMessagePush, Integer> {

	/**
	 * 获取默认配置
	 * @Title: getDefault  
	 * @return
	 * @throws GlobalException 全局异常    
	 * @return: SystemMessagePush
	 */
	SystemMessagePush getDefault() throws GlobalException;

}

/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.member.dao.UserTotalDao;
import com.jeecms.member.domain.UserTotal;
import com.jeecms.member.service.UserTotalService;
import com.jeecms.common.base.service.BaseServiceImpl;

/**
 * 实现类
* @author ljw
* @version 1.0
* @date 2019-09-23
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserTotalServiceImpl extends BaseServiceImpl<UserTotal,UserTotalDao, Integer>  
		implements UserTotalService {

	@Override
	public UserTotal findByUserId(Integer userId) {
		return dao.findByUserId(userId);
	}

 
}
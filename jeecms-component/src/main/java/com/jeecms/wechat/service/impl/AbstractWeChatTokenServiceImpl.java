package com.jeecms.wechat.service.impl;


import org.springframework.stereotype.Service;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.wechat.dao.AbstractWeChatTokenDao;
import com.jeecms.wechat.domain.AbstractWeChatToken;
import com.jeecms.wechat.service.AbstractWeChatTokenService;

/**
 * @Description:AbstractWeChatTokenServiceImpl
 * @author: qqwang
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
@Service
public class AbstractWeChatTokenServiceImpl extends BaseServiceImpl<AbstractWeChatToken,AbstractWeChatTokenDao, Integer>  implements AbstractWeChatTokenService{

	@Override
	public AbstractWeChatToken findByAppId(String appId){
		return dao.findByAppId(appId);
	}
	
}

/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.weibo.dao.WeiboAppConfigDao;
import com.jeecms.weibo.domain.WeiboAppConfig;
import com.jeecms.weibo.service.WeiboAppConfigService;
import com.jeecms.common.base.service.BaseServiceImpl;

/**
 * 微博应用实现类
* @author ljw
* @version 1.0
* @date 2019-06-15
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WeiboAppConfigServiceImpl extends BaseServiceImpl<WeiboAppConfig,WeiboAppConfigDao, Integer>  
			implements WeiboAppConfigService {

	@Override
	public WeiboAppConfig getBySiteId(Integer siteId) {
		return dao.findBySiteIdAndHasDeletedFalse(siteId);
	}

 
}
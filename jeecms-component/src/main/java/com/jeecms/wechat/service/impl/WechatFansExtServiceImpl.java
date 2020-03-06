/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.wechat.dao.WechatFansExtDao;
import com.jeecms.wechat.domain.WechatFansExt;
import com.jeecms.wechat.service.WechatFansExtService;
import com.jeecms.common.base.service.BaseServiceImpl;

/**
 * 微信粉丝扩展实现类
* @author ljw
* @version 1.0
* @date 2019-05-29
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatFansExtServiceImpl extends BaseServiceImpl<WechatFansExt,WechatFansExtDao, Integer>  
				implements WechatFansExtService {

	@Override
	public void deleteAllFansExt(List<String> openids) {
		dao.deleteAllFansExt(openids);
	}

}
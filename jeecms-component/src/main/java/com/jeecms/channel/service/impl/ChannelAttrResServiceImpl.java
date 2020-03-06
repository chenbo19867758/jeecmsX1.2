/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.channel.dao.ChannelAttrResDao;
import com.jeecms.channel.domain.ChannelAttrRes;
import com.jeecms.channel.service.ChannelAttrResService;
import com.jeecms.common.base.service.BaseServiceImpl;

/**
 * 栏目自定义属性service实现类
 * @author: chenming
 * @date:   2019年6月28日 下午5:31:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChannelAttrResServiceImpl extends BaseServiceImpl<ChannelAttrRes, ChannelAttrResDao, Integer>
		implements ChannelAttrResService {

}
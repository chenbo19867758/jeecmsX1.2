/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.channel.dao;

import java.util.List;

import com.jeecms.channel.domain.ChannelAttr;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * 栏目自定义属性dao接口
 * @author: chenming
 * @date:   2019年6月28日 下午5:20:32
 */
public interface ChannelAttrDao extends IBaseDao<ChannelAttr, Integer> {
	
	List<ChannelAttr> findByChannelId(Integer channelId);
}

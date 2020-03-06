/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import java.util.List;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.exception.GlobalException;

/**
 * 栏目监听器
 * 
 * @author: tom
 * @date: 2019年4月8日 上午9:40:15
 */
public interface ChannelListener {
	/**
	 * 栏目物理删除操作
	 * 
	 * @Title: beforeChannelPhysicDelete
	 * @param ids 栏目id
	 * @throws GlobalException GlobalException
	 */
	void beforeChannelDelete(Integer[] ids) throws GlobalException;

	/**
	 * 栏目保存之后的数据处理
	 * 
	 * @Title: afterChannelSave
	 * @param c 栏目
	 * @throws GlobalException GlobalException
	 * @return: void
	 */
	void afterChannelSave(Channel c) throws GlobalException;
	
	/**
	 * 栏目加入回收站之后的数据处理
	 * @Title: beforeChannelRecycle  
	 * @param channels	栏目集合
	 * @throws GlobalException      
	 * @return: void
	 */
	void afterChannelRecycle(List<Channel> channels) throws GlobalException;
	
	/**
	 * 栏目变更
	 * @Title: afterChannelChange
	 * @param c
	 * @throws GlobalException
	 * @return: void
	 */
	void afterChannelChange(Channel c) throws GlobalException;
	
}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentChannel;

/**
 * 内容栏目关联service接口
 * 
 * @author: chenming
 * @date: 2019年5月15日 下午5:13:07
 */
public interface ContentChannelService extends IBaseService<ContentChannel, Integer> {

	/**
	 * 根据栏目ID查询引用内容
	 * 
	 * @Title: Count
	 * @param channelId 栏目ID
	 * @param list      创建方式集合
	 * @return List 集合
	 * @throws GlobalException 异常
	 */
	List<ContentChannel> countQuote(Integer channelId, List<Integer> list) throws GlobalException;
	
	/**
	 * 修改内容时同步修改内容栏目引用
	 * @Title: update  
	 * @param content
	 * @throws GlobalException      
	 * @return: void
	 */
	void update(Content content) throws GlobalException;
	
	List<ContentChannel> findByChannelIds(Integer[] channelId);
}

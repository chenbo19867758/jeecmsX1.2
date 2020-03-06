/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.channel.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.ChannelTxt;

/**
 * 栏目内容service接口
 * @author: chenming
 * @date:   2019年6月25日 上午9:37:16
 */
public interface ChannelTxtService extends IBaseService<ChannelTxt, Integer> {

	/**
	 * 新增多个栏目内容
	 * @Title: ChannelTxt  
	 * @param txts	栏目内容list集合
	 * @param channel	栏目对象
	 * @throws GlobalException  全局异常    
	 * @return: void
	 */
	List<ChannelTxt> save(List<ChannelTxt> txts,Channel channel) throws GlobalException;
	
	/**
	 * 根据栏目id查询该栏目的富文本的内容集合
	 * @Title: findByChannelId  
	 * @param channelId	栏目id
	 * @return: List
	 */
	List<ChannelTxt> findByChannelId(Integer channelId);
	
	/**
	 * 将json转换成Map方便前台处理数据
	 * @Title: toChannelTxtMap  
	 * @param json		接受前台的json串
	 * @param modelId	模型id
	 * @throws GlobalException	全局异常      
	 * @return: Map
	 */
	Map<String,String> toChannelTxtMap(JSONObject json,Integer modelId)  throws GlobalException;
}

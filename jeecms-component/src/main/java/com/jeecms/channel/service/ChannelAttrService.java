/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.ChannelAttr;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.GlobalConfig;

/**
 * 内容属性service接口
 * 
 * @author: chenming
 * @date: 2019年7月10日 上午9:58:44
 */
public interface ChannelAttrService extends IBaseService<ChannelAttr, Integer> {

	/**
	 * 通过栏目id查询栏目自定义属性集合
	 * 
	 * @Title: findByChannelId
	 * @param channelId 栏目id
	 * @return: List
	 */
	List<ChannelAttr> findByChannelId(Integer channelId);

	/**
	 * 用户栏目serviceImpl中组装channelAttr数据
	 * 
	 * @Title: splicChannelAttr
	 * @param channelAttrs 栏目自定义属性对象集合
	 * @param channelId    栏目id
	 * @param globalConfig 全局配置
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<ChannelAttr> splic(List<ChannelAttr> channelAttrs, Integer channelId, GlobalConfig globalConfig)
			throws GlobalException;

	/**
	 * 将前端传入的json转换成栏目自定义属性集合
	 * 
	 * @Title: initChannelAttr
	 * @param ject    前端传入的json
	 * @param modelId 模型id
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<ChannelAttr> init(JSONObject ject, Integer modelId,Integer channelId,String groupType) throws GlobalException;

	/**
	 * 初始化拥有默认数值的自定义字段
	 * 
	 * @Title: splicDefValue
	 * @param modelId  模型id值
	 * @param attrs    传入的栏目自定义集合
	 * @param isUpdate 是否修改
	 * @param channel  栏目对象
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<ChannelAttr> splicDefValue(Integer modelId, List<ChannelAttr> attrs, boolean isUpdate, Channel channel)
			throws GlobalException;
}

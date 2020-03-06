/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.dao.ChannelTxtDao;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelTxtService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.ChannelTxt;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.service.CmsModelItemService;

/**
 * 栏目内容service实现类
 * 
 * @author: chenming
 * @date: 2019年6月25日 上午9:38:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChannelTxtServiceImpl extends BaseServiceImpl<ChannelTxt, ChannelTxtDao, Integer>
		implements ChannelTxtService {

	@Override
	public List<ChannelTxt> save(List<ChannelTxt> txts,Channel channel) throws GlobalException {
		// 该方法是后台调用会进行控制，所以此处出现channel为空的概率可以忽略
		if (txts != null && txts.size() > 0) {
			for (ChannelTxt channelTxt : txts) {
				channelTxt.setChannelId(channel.getId());
				channelTxt.setChannel(channel);
			}
			return super.saveAll(txts);
		}
		return null;
	}

	@Override
	public List<ChannelTxt> findByChannelId(Integer channelId) {
		return dao.findByChannelId(channelId);
	}

	@Override
	public Map<String,String> toChannelTxtMap(JSONObject json, Integer modelId) 
			throws GlobalException {
		// 根据模型id查询出模型的字段
		List<CmsModelItem> items = cmsModelItemService.findByModelId(modelId);
		// 选出字段中类型为富文本的
		items = items.stream().filter(item -> CmsModelConstant.RICH_TEXT.equals(item.getDataType()))
				.collect(Collectors.toList());
		Map<String,String> txtMap = new LinkedHashMap<String, String>();
		if (items != null && items.size() > 0) {
			for (CmsModelItem cmsModelItem : items) {
				String field = cmsModelItem.getField();
				String value = json.getString(field);
				if (StringUtils.isNotBlank(value)) {
					txtMap.put(field, value);
				}
			}
		}
		return txtMap;
	}

	@Autowired
	private CmsModelItemService cmsModelItemService;

}
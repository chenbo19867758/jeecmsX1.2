/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.auth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.dao.UserModelSortDao;
import com.jeecms.auth.domain.UserModelSort;
import com.jeecms.auth.service.UserModelSortService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.ChannelContentTpl;
import com.jeecms.channel.service.ChannelContentTplService;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModel;

/**
 * 实现类
* @author ljw
* @version 1.0
* @date 2019-12-13
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserModelSortServiceImpl extends BaseServiceImpl<UserModelSort,UserModelSortDao, Integer>  
		implements UserModelSortService {
	
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ChannelContentTplService channelContentTplService;
	
	@Override
	public List<CmsModel> sort(List<CmsModel> models, Integer channelId, Integer userId) 
			throws GlobalException {
		// 判断是否底层栏目,不是底层栏目直接返回空集合
		List<CmsModel> modelList = new ArrayList<CmsModel>();
		Channel channel = channelService.findById(channelId);
		// 得到栏目勾选的内容模型
		List<ChannelContentTpl> tpls = channelContentTplService.findByChannelIdSelect(channelId, true);
		// 不是底层栏目，模型为空，勾选模型为空，都返回空集合
		if (channel == null || !channel.getIsBottom() || tpls.isEmpty() || models.isEmpty()) {
			return modelList;
		}
		// 从现有的模型与勾选的模型对比，得到勾选的模型，防止勾选了但是删除的模型还在里面
		List<Integer> ids = tpls.stream().map(ChannelContentTpl::getModelId).collect(Collectors.toList());
		models = models.stream().filter(x -> ids.contains(x.getId())).collect(Collectors.toList());
		// 得到模型使用频率，取最近前一天统计的数据
		List<UserModelSort> sorts = getSortList(userId);
		// 不为空才排序
		if (!sorts.isEmpty()) {
			// 根据用户使用频率排序
			CopyOnWriteArrayList<CmsModel> models2 = new CopyOnWriteArrayList<CmsModel>(models);
			for (UserModelSort userModelSort : sorts) {
				Optional<CmsModel> optional = models2.stream().filter(x -> x.getId()
						.equals(userModelSort.getModelId()))
						.findFirst();
				if (optional.isPresent()) {
					modelList.add(optional.get());
					models2.remove(optional.get());
				}
			}
			// 排序完成，将剩余的勾选模型放到末尾即可
			modelList.addAll(models2);
			return modelList;
		}
		return models;
	}

	@Override
	public List<UserModelSort> getSortList(Integer userId) throws GlobalException {
		return dao.getSortList(userId);
	}
 
}
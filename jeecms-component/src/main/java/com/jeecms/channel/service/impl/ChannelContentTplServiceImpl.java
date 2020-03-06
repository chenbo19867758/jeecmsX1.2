package com.jeecms.channel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.channel.dao.ChannelContentTplDao;
import com.jeecms.channel.domain.ChannelContentTpl;
import com.jeecms.channel.service.ChannelContentTplService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SiteModelTpl;
import com.jeecms.system.service.SiteModelTplService;

/**
 * 栏目内容目标service实现类
 * @author: chenming
 * @date:   2019年4月18日 上午10:08:10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChannelContentTplServiceImpl extends BaseServiceImpl<ChannelContentTpl, ChannelContentTplDao, Integer>
		implements ChannelContentTplService {

	@Autowired
	private SiteModelTplService siteModelTplService;
	
	@Override
	public List<ChannelContentTpl> findByChannelId(Integer channelId) throws GlobalException {
		List<ChannelContentTpl> cList = dao.findByChannelId(channelId);
		return cList;
	}

	@Override
	public ChannelContentTpl findByChannelIdAndModelId(Integer channelId, Integer modelId) {
		return dao.findByChannelIdAndModelId(channelId, modelId);
	}

	@Override
	public List<ChannelContentTpl> save(List<ChannelContentTpl> contentTpls,Integer channelId,Integer siteId) 
			throws GlobalException {
		if (contentTpls != null && contentTpls.size() > 0) {
			for (ChannelContentTpl channelContentTpl : contentTpls) {
				channelContentTpl.setId(null);
				channelContentTpl.setChannelId(channelId);
				channelContentTpl.setSortNum(10);
				channelContentTpl.setSelect(channelContentTpl.getSelect() != null 
						? channelContentTpl.getSelect()
								: false);
			}
		} else {
			contentTpls = new ArrayList<ChannelContentTpl>();
			List<SiteModelTpl> siteModelTpls = siteModelTplService.findBySiteId(siteId);
			for (SiteModelTpl siteModelTpl : siteModelTpls) {
				ChannelContentTpl contentTpl = new ChannelContentTpl();
				contentTpl.setTplPc(siteModelTpl.getPcTplPath());
				contentTpl.setTplMobile(siteModelTpl.getMobileTplPath());
				contentTpl.setModelId(siteModelTpl.getModelId());
				contentTpl.setChannelId(channelId);
				contentTpl.setSortNum(10);
				contentTpl.setSelect(contentTpl.getSelect() != null 
						? contentTpl.getSelect()
								: false);
				contentTpls.add(contentTpl);
			}
		}
		List<ChannelContentTpl> channelContentTpls = super.saveAll(contentTpls);
		super.flush();
		return channelContentTpls;
	}
	
	
	
	@Override
	public List<ChannelContentTpl> findByChannelIdSelect(Integer channelId, Boolean select) throws GlobalException {
		return dao.findByChannelIdAndSelect(channelId, select);
	}
}

/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.member.dao.MemberGroupDao;
import com.jeecms.member.domain.MemberGroup;
import com.jeecms.member.service.MemberGroupService;

/**
 * 会员组管理 service实现层
 * 
 * @author: wulongwei
 * @date: 2019年4月15日 上午10:04:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberGroupServiceImpl extends BaseServiceImpl<MemberGroup, MemberGroupDao, Integer>
		implements MemberGroupService {

	@Autowired
	private ChannelService channelService;

	@Override
	public ResponseInfo saveMemberGroupInfo(MemberGroup memberGroup) throws GlobalException {
		// 判断浏览权限
		if (!memberGroup.getIsAllChannelView()) {
			List<Channel> channels = channelService.findAllById(memberGroup.getViews());
			memberGroup.setViewChannels(channels);
		}
		// 判断投稿权限
		if (!memberGroup.getIsAllChannelContribute()) {
			List<Channel> channels = channelService.findAllById(memberGroup.getContributes());
			memberGroup.setContributeChannels(channels);
		}
		if (memberGroup.getIsDefault() == null) {
			memberGroup.setIsDefault(false);
		}
		MemberGroup groupInfo = dao.findByIsDefaultAndHasDeleted(true, false);
		// 如果已经存在默认的会员组信息，且新增中会员组信息为默认，则将原来存在的改为非默认
		if (groupInfo != null && memberGroup.getIsDefault()) {
			groupInfo.setIsDefault(false);
			super.update(groupInfo);
		}
		super.save(memberGroup);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo updateMemberGroupInfo(MemberGroup memberGroup) throws GlobalException {
		MemberGroup groupInfo = dao.findByIsDefaultAndHasDeleted(true, false);
		// 判断浏览权限
		if (!memberGroup.getIsAllChannelView()) {
			List<Channel> channels = channelService.findAllById(memberGroup.getViews());
			memberGroup.setViewChannels(channels);
		}
		// 判断投稿权限
		if (!memberGroup.getIsAllChannelContribute()) {
			List<Channel> channels = channelService.findAllById(memberGroup.getContributes());
			memberGroup.setContributeChannels(channels);
		}
		// 如果已经存在默认的会员组信息，且修改中会员组信息为默认，则将原来存在的改为非默认
		Boolean flag = groupInfo != null && memberGroup.getIsDefault() 
				&& groupInfo.getId() != memberGroup.getId();
		if (flag) {
			groupInfo.setIsDefault(false);
			super.update(groupInfo);
		}
		super.update(memberGroup);
		return new ResponseInfo();
	}

}

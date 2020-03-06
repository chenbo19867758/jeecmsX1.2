/***
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.dao.SysMessageDao;
import com.jeecms.system.domain.SysMessage;
import com.jeecms.system.domain.dto.SysMessageDto;
import com.jeecms.system.service.SysMessageService;

/**
 * 系統消息Service
 * @author ljw
 * @version 1.0
 * @date 2018-07-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMessageServiceImpl extends BaseServiceImpl<SysMessage, SysMessageDao, Integer>
		implements SysMessageService {
	
	@Autowired
	private CoreUserService coreUserService;
	
	@Override
	public ResponseInfo getMessagePage(Pageable pageable) {
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		params.put("EQ_status_Integer", new String[] { "1" });
		Page<SysMessage> page = super.getPage(params, pageable, false);
		return new ResponseInfo(page);
	}

	@Override
	public ResponseInfo deleteMessage(Integer[] ids) throws GlobalException {
		List<Integer> list = Arrays.asList(ids);
		List<SysMessage> mesList = super.findAllById(list);
		for (SysMessage sysMessage : mesList) {
			sysMessage.setStatus(2);
			super.update(sysMessage);
		}
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo saveMessage(SysMessageDto dto) throws GlobalException {
		List<SysMessage> array = new ArrayList<SysMessage>(10);
		if (dto.getSendIds().isEmpty()) {
			SysMessage sysMessage = new SysMessage();
			sysMessage.setSendUserName("系统");
			sysMessage.setTitle(dto.getTitle());
			sysMessage.setContent(dto.getContent());
			sysMessage.setStatus(SysMessage.MESSAGE_STATUS_NORMAL);
			// 获取接收类型
			Integer recTargetType = dto.getRecTargetType();
			switch (recTargetType) {
				case SysMessage.TARGETTYPE_ALL:
					//全部
					sysMessage.setRecTargetType(recTargetType);
					super.save(sysMessage);
					break;
				case SysMessage.TARGETTYPE_ALL_ADMIN:	
					//全部管理员
					sysMessage.setRecTargetType(recTargetType);
					super.save(sysMessage);
					break;
				case SysMessage.TARGETTYPE_ALL_MEMBER:
					//全部会员
					sysMessage.setRecTargetType(recTargetType);
					super.save(sysMessage);
					break;
				case SysMessage.TARGETTYPE_ORG:
					//组织
					sysMessage.setOrgId(dto.getOrgId());
					sysMessage.setRecTargetType(recTargetType);
					super.save(sysMessage);
					break;
				case SysMessage.TARGETTYPE_APPOINT_ADMIN:  
					// 指定用户ID
					List<Integer> list = dto.getUserId();
					if (list != null && !list.isEmpty()) {
						for (Integer userId : list) {
							SysMessage sys = new SysMessage();
							sys.setSendUserName("系统");
							sys.setTitle(dto.getTitle());
							sys.setContent(dto.getContent());
							sys.setStatus(SysMessage.MESSAGE_STATUS_NORMAL);
							sys.setRecTargetType(recTargetType);
							sys.setUserId(userId);
							sys.setCoreUser(coreUserService.findById(userId));
							array.add(sys);
						}
						super.saveAll(array);
					}
					break;
				case SysMessage.TARGETTYPE_MEMBER_LELVEL:
					//会员等级
					sysMessage.setMemeberLevelId(dto.getMemeberLevelId());
					sysMessage.setRecTargetType(recTargetType);
					super.save(sysMessage);
					break;	
				case SysMessage.TARGETTYPE_MEMBER_GROUP:
					//会员组
					sysMessage.setMemeberGroupId(dto.getMemeberGroupId());;
					sysMessage.setRecTargetType(recTargetType);
					super.save(sysMessage);
					break;
				case SysMessage.TARGETTYPE_APPOINT_MEMBER:
					//指定会员
					List<Integer> members = dto.getMemeberId();
					if (members != null && !members.isEmpty()) {
						for (Integer memberId : members) {
							SysMessage sys = new SysMessage();
							sys.setSendUserName("系统");
							sys.setTitle(dto.getTitle());
							sys.setContent(dto.getContent());
							sys.setStatus(SysMessage.MESSAGE_STATUS_NORMAL);
							sys.setRecTargetType(recTargetType);
							sys.setMemeberId(memberId);
							array.add(sys);
						}
						super.saveAll(array);
					}
					break;
				default:
					break;
			}
		} else {
			for (Integer sendUserId : dto.getSendIds()) {
				CoreUser user = coreUserService.findById(sendUserId);
				SysMessage sysMessage = new SysMessage();
				sysMessage.setSendUserName(user.getUsername());
				sysMessage.setTitle(dto.getTitle());
				sysMessage.setContent(dto.getContent());
				sysMessage.setStatus(SysMessage.MESSAGE_STATUS_NORMAL);
				// 获取接收类型
				Integer recTargetType = dto.getRecTargetType();
				switch (recTargetType) {
					case SysMessage.TARGETTYPE_ALL:
						//全部
						sysMessage.setRecTargetType(recTargetType);
						super.save(sysMessage);
						break;
					case SysMessage.TARGETTYPE_ALL_ADMIN:	
						//全部管理员
						sysMessage.setRecTargetType(recTargetType);
						super.save(sysMessage);
						break;
					case SysMessage.TARGETTYPE_ALL_MEMBER:
						//全部会员
						sysMessage.setRecTargetType(recTargetType);
						super.save(sysMessage);
						break;
					case SysMessage.TARGETTYPE_ORG:
						//组织
						sysMessage.setOrgId(dto.getOrgId());
						sysMessage.setRecTargetType(recTargetType);
						super.save(sysMessage);
						break;
					case SysMessage.TARGETTYPE_APPOINT_ADMIN:  
						// 指定用户ID
						List<Integer> list = dto.getUserId();
						if (list != null && !list.isEmpty()) {
							for (Integer userId : list) {
								SysMessage sys = new SysMessage();
								sys.setSendUserName(user.getUsername());
								sys.setTitle(dto.getTitle());
								sys.setContent(dto.getContent());
								sys.setStatus(SysMessage.MESSAGE_STATUS_NORMAL);
								sys.setRecTargetType(recTargetType);
								sys.setUserId(userId);
								array.add(sys);
							}
							super.saveAll(array);
						}
						break;
					case SysMessage.TARGETTYPE_MEMBER_LELVEL:
						//会员等级
						sysMessage.setMemeberLevelId(dto.getMemeberLevelId());
						sysMessage.setRecTargetType(recTargetType);
						super.save(sysMessage);
						break;	
					case SysMessage.TARGETTYPE_MEMBER_GROUP:
						//会员组
						sysMessage.setMemeberGroupId(dto.getMemeberGroupId());;
						sysMessage.setRecTargetType(recTargetType);
						super.save(sysMessage);
						break;
					case SysMessage.TARGETTYPE_APPOINT_MEMBER:
						//指定会员
						List<Integer> members = dto.getMemeberId();
						if (members != null && !members.isEmpty()) {
							for (Integer memberId : members) {
								SysMessage sys = new SysMessage();
								sys.setSendUserName(user.getUsername());
								sys.setTitle(dto.getTitle());
								sys.setContent(dto.getContent());
								sys.setStatus(SysMessage.MESSAGE_STATUS_NORMAL);
								sys.setRecTargetType(recTargetType);
								sys.setMemeberId(memberId);
								array.add(sys);
							}
							super.saveAll(array);
						}
						break;
					default:
						break;
				}
			}
		}
		return new ResponseInfo();
	}
	
}
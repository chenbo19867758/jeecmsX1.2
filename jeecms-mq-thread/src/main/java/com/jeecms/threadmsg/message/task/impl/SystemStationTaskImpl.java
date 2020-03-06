package com.jeecms.threadmsg.message.task.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.message.MqConstants;
import com.jeecms.message.dto.CommonMqConstants;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.domain.dto.SysMessageDto;
import com.jeecms.system.service.MessageTplDetailsService;
import com.jeecms.system.service.SysMessageService;
import com.jeecms.threadmsg.common.MessageInfo;
import com.jeecms.threadmsg.message.task.Task;

/**
 * @Description: 站内信任务
 * @author: ztx
 * @date: 2019年1月21日 下午4:45:38
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Repository("sysStationTask")
public class SystemStationTaskImpl implements Task {

	@Override
	public boolean exec(Object msg) throws Exception {
		if (msg instanceof MessageInfo) {
			MessageInfo msgInfo = (MessageInfo) msg;
			String title = msgInfo.getTitle();
			String content = msgInfo.getTextContent();
			String mesCode = msgInfo.getMessageCode();
			MessageTplDetails emailMesTplDetail = null;
			if (mesCode != null) {
				emailMesTplDetail = mesTplDetailService.findByCodeAndType(mesCode, MessageTplDetails.MESTYPE_SITE,
						MessageTplDetails.MESTYPE_SITE);
			}
			if(StringUtils.isBlank(title)&&emailMesTplDetail!=null){
				title = emailMesTplDetail.getMesTitle();
			}
			if(StringUtils.isBlank(content)&&emailMesTplDetail!=null){
				content = emailMesTplDetail.getMesContent();
				JSONObject json = msgInfo.getData();
				if (json != null) {
					json = json.getJSONObject(CommonMqConstants.EXT_DATA_KEY_SYSTEM);
					/**占位符值替换*/
					if(json!=null){
						Set<String>places =json.keySet();
						for(String p:places){
							if (p.startsWith("${")) {
								content = content.replace(p, json.getString(p));
							} else {
								content = content.replace("${"+p+"}", json.getString(p));
							}
						}
					}
				}
			}
			if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
				return false;
			}
			SysMessageDto messageDto = this.initMessageDto(msgInfo, title, content);
			if (messageDto == null) {
				return false;
			}
			stationService.saveMessage(messageDto);
			return true;
		}
		return false;
	}

	private SysMessageDto initMessageDto(MessageInfo info,String title,String content) {
		SysMessageDto dto = null;
		Integer targetType = info.getTargetType();
		switch (targetType) {
			case MqConstants.USER_ALL:
				dto = new SysMessageDto(title, content, targetType);
				break;
			case MqConstants.MANAGER_ALL:
				dto = new SysMessageDto(title, content, targetType);
				break;
			case MqConstants.MEMBER_ALL:
				dto = new SysMessageDto(title, content, targetType);
				break;
			case MqConstants.ASSIGN_ORG:
				List<Integer> orgs = new ArrayList<Integer>();
				orgs.add(info.getOrgId());
				List<CoreUser> coreUsers = coreUserService.findList(true, orgs, null, null, true, Short.valueOf("1"), null, 
						null, null, null, null);
				List<Integer> userIds = new ArrayList<Integer>();
				if (coreUsers != null && coreUsers.size() > 0) {
					userIds = coreUsers.stream().map(CoreUser::getId).collect(Collectors.toList());
				}
				dto = new SysMessageDto(title, content, targetType, userIds);
				break;
			case MqConstants.ASSIGN_MANAGER:
				dto = new SysMessageDto(title, content, targetType, info.getReceiveIds());
				break;
			case MqConstants.ASSIGN_MANAGER_LEVEL:
				dto = new SysMessageDto(title, content, targetType, info.getMemberLevelId(), null, null, null);
				break;
			case MqConstants.ASSIGN_MEMBER_GROUP:
				dto = new SysMessageDto(title, content, targetType, null, info.getMemberGroupId(), null, null);
				break;
			case MqConstants.ASSIGN_MEMBER:
				dto = new SysMessageDto(title, content, targetType, null, null, null, info.getReceiveIds());
				break;
			default:
				break;
		}
		return dto;
	}
	
	@Override
	public int operation() {
		return MqConstants.MESSAGE_QUEUE_SYSTEM_STATION_MAIL;
	}

	/** 站内信Service */
	@Autowired
	private SysMessageService stationService;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private MessageTplDetailsService mesTplDetailService;
	
}

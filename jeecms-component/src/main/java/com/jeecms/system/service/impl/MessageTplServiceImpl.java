/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.constants.MessageTplCodeConstants;
import com.jeecms.system.dao.MessageTplDao;
import com.jeecms.system.domain.MessageTpl;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.service.MessageTplDetailsService;
import com.jeecms.system.service.MessageTplService;

/**
 * 消息模板service层实现
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年4月26日 上午9:55:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTplServiceImpl extends BaseServiceImpl<MessageTpl, MessageTplDao, Integer>
		implements MessageTplService {

	/**
	 * 保存
	 * @Title: save
	 * @param: @param bean
	 * @param: @return
	 * @return: T
	 */
	@Override
	public ResponseInfo saveMessageTpl(MessageTpl bean) throws GlobalException {
		bean = super.save(bean);
		List<MessageTplDetails> list = new ArrayList<MessageTplDetails>();
		for (MessageTplDetails tplDetil : bean.getDetails()) {
			tplDetil.setMesTplId(bean.getId());;
			// 如果为开启状态下
			if (tplDetil.getIsOpen().equals(true)) {
				checkInformation(bean, tplDetil, MessageTpl.DELETE_RETURN_ERROR);
				list.add(tplDetil);
			} else {
				// 如果为不开启状态下
				list.add(tplDetil);
			}
		}
		messageTplDetailsService.saveAll(list);
		return new ResponseInfo();
	}
	
	/**
	 * 先修改详细消息，再修改公共消息
	 */
	@Override
	public MessageTpl updateMessageTpl(MessageTpl bean) throws GlobalException {
		for (MessageTplDetails tplDetil : bean.getDetails()) {
			// 如果为开启状态下
			if (tplDetil.getIsOpen().equals(true)) {
				checkInformation(bean, tplDetil, MessageTpl.RETURN_ERROR);
			}
			messageTplDetailsService.update(tplDetil);
		}
		bean.getDetails().clear();
		bean = super.update(bean);
		return bean;
	}


	@Override
	public MessageTpl findByMesCode(String mesCode) {
		return dao.findByMesCodeAndHasDeleted(mesCode, false);
	}
	

	/**
	 * 查询手机模板消息是否开启
	 */
	@Override
	public Boolean findByMesCodeAndMesType() {
		boolean phoneIsOpen = false;
		MessageTpl messageTpl = dao.findByMesCodeAndHasDeleted(
				MessageTplCodeConstants.USER_REGISTER_VALIDATE_CODE_TPL, false);
		if (messageTpl != null && messageTpl.getDetails() != null) {
			for (MessageTplDetails tplDetil : messageTpl.getDetails()) {
				// 获取手机的模版消息
				if (MessageTplDetails.MESTYPE_PHONE.equals(tplDetil.getMesType())) {
					phoneIsOpen = tplDetil.getIsOpen();
				}
			}
		}
		return phoneIsOpen;
	}

	/**
	 * 校验用户填写的信息type为1时，表示只返回错误信息。type为2时，表示删除模板信息，并返回错误信息
	 */
	public void checkInformation(MessageTpl bean, MessageTplDetails tplDetil, Integer type)
			throws GlobalException {
		switch (tplDetil.getMesType()) {
			case MessageTplDetails.MAIL:
				checkMail(bean, tplDetil, type);
				break;
			case MessageTplDetails.PHONE:
				chackPhone(bean, tplDetil, type);
				break;
			default:
		}
	}

	/**
	 * 邮件消息的 模板标题不能为空、消息内容不能为空
	 */
	public void checkMail(MessageTpl bean, MessageTplDetails tplDetil, Integer type) throws GlobalException {
		if (StringUtils.isBlank(tplDetil.getMesTitle())) {
			if (MessageTpl.DELETE_RETURN_ERROR.equals(type)) {
				super.physicalDelete(bean.getId());
			}
			throw new GlobalException(new WeChatExceptionInfo(SettingErrorCodeEnum.MESTITLE_NOTNULL
					.getCode(), SettingErrorCodeEnum.MESTITLE_NOTNULL.getDefaultMessage()));
		}

		if (StringUtils.isBlank(tplDetil.getMesContent())) {
			if (MessageTpl.DELETE_RETURN_ERROR.equals(type)) {
				super.physicalDelete(bean.getId());
			}
			throw new GlobalException(new WeChatExceptionInfo(SettingErrorCodeEnum.MESSAGECONTENT_NOTNULL
					.getCode(), SettingErrorCodeEnum.MESSAGECONTENT_NOTNULL.getDefaultMessage()));
		}
	}

	/**
	 * 手机消息的 模板ID不能为空
	 */
	public void chackPhone(MessageTpl bean, MessageTplDetails tplDetil, Integer type) throws GlobalException {
		if (StringUtils.isBlank(tplDetil.getTplId())) {
			if (MessageTpl.DELETE_RETURN_ERROR.equals(type)) {
				super.physicalDelete(bean.getId());
			}
			throw new GlobalException(new WeChatExceptionInfo(SettingErrorCodeEnum.TPLID_NOTNULL.getCode(),
					SettingErrorCodeEnum.TPLID_NOTNULL.getDefaultMessage()));
		}
	}
	
	@Autowired
    private MessageTplDetailsService messageTplDetailsService;
}

package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.wechat.Const;
import com.jeecms.wechat.dao.WechatReplyClickDao;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatReplyClick;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.service.WechatMaterialService;
import com.jeecms.wechat.service.WechatReplyClickService;
import com.jeecms.wechat.service.WechatReplyContentService;

/**
 * 事件触发service实现类
 * 
 * @author: chenming
 * @date: 2019年6月13日 下午3:11:25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatReplyClickServiceImpl extends BaseServiceImpl<WechatReplyClick, WechatReplyClickDao, Integer>
		implements WechatReplyClickService {

	@Autowired
	WechatMaterialService wechatMaterialService;
	@Autowired
	WechatReplyContentService wechatReplyContentService;

	@Override
	public WechatReplyClick findByReplyTypeAndAppId(Integer replyType, String appid) throws GlobalException {
		return dao.findByReplyTypeAndAppIdAndHasDeleted(replyType, appid, false);
	}

	@Override
	public void saveKeyWord(WechatReplyClick wechatReplyClick) throws GlobalException {
		WechatReplyClick click = dao.findByReplyTypeAndAppIdAndHasDeleted(
				wechatReplyClick.getReplyType(), wechatReplyClick.getAppId(), false);
		if (wechatReplyClick.getWechatReplyContent() == null) {
			return;
		}
		WechatReplyContent bean = wechatReplyClick.getWechatReplyContent();
		bean = this.initContent(bean);
		WechatReplyContent content = wechatReplyContentService.save(bean);
		if (click != null) {
			wechatReplyContentService.delete(click.getReplyContentId());
			click.setReplyContentId(content.getId());
			click.setWechatReplyContent(content);
			super.update(click);
		} else {
			wechatReplyClick.setReplyContentId(content.getId());
			wechatReplyClick.setWechatReplyContent(content);
			super.save(wechatReplyClick);
		}
	}

	@Override
	public void updateKeyWord(WechatReplyClick wechatReplyClick) throws GlobalException {
		WechatReplyClick wClick = super.findById(wechatReplyClick.getId());
		if (wClick != null) {
			wechatReplyContentService.delete(wClick.getReplyContentId());
			if (wechatReplyClick.getWechatReplyContent() != null) {
				WechatReplyContent bean = wechatReplyClick.getWechatReplyContent();
				bean = this.initContent(bean);
				WechatReplyContent content = wechatReplyContentService.save(bean);
				wClick.setReplyContentId(content.getId());
				wClick.setWechatReplyContent(content);
			} else {
				wClick.setReplyContentId(null);
				wClick.setWechatReplyContent(null);
			}
			super.update(wClick);
		}
		
	}
	
	private WechatReplyContent initContent(WechatReplyContent content) throws GlobalException {
		content.setMsgType(null);
		if (StringUtils.isNotBlank(content.getContent())) {
			content.setMediaId(null);
			// 文本类型
			content.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_TEXT);
		} else {
			if (content.getMediaId() == null) {
				// 回复内容不能为空
				throw new GlobalException(
						new SystemExceptionInfo(
							SettingErrorCodeEnum.CONTENT_NOTNULL.getCode(),
							SettingErrorCodeEnum.CONTENT_NOTNULL.getDefaultMessage()));
			} else {
				content.setContent(null);
				// 媒体类型
				WechatMaterial wechatMaterial = wechatMaterialService.findById(content.getMediaId());
				String mediaType = wechatMaterial.getMediaType();
				if (Const.Mssage.REQ_MESSAGE_TYPE_IMAGE.equals(mediaType)
						||
					Const.Mssage.REQ_MESSAGE_TYPE_VOICE.equals(mediaType)
						||
					Const.Mssage.REQ_MESSAGE_TYPE_VIDEO.equals(mediaType)) {
					content.setMsgType(wechatMaterial.getMediaType());
				}
			}

		}
		if (StringUtils.isBlank(content.getMsgType())) {
			throw new GlobalException(
					new SystemExceptionInfo(
							SettingErrorCodeEnum.REPLY_TYPE_ERROR.getCode(),
							SettingErrorCodeEnum.REPLY_TYPE_ERROR.getDefaultMessage()));
		}
		return content;
	}

	@Override
	public List<Integer> findByAppId(String appId) {
		List<WechatReplyClick> clickList = dao.findByAppIdAndHasDeleted(appId, false);
		List<Integer> ids = new ArrayList<Integer>();
		for (WechatReplyClick click : clickList) {
			ids.add(click.getReplyContentId());
		}
		return ids;
	}

}
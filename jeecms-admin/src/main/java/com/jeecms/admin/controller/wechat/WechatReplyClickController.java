package com.jeecms.admin.controller.wechat;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatReplyClick;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.domain.WechatReplyClick.SaveClick;
import com.jeecms.wechat.domain.WechatReplyClick.UpdateClick;
import com.jeecms.wechat.service.WechatReplyClickService;
import com.jeecms.wechat.service.WechatReplyContentService;

@RequestMapping("/wechatreplyclick")
@RestController
public class WechatReplyClickController extends BaseController<WechatReplyClick, Integer> {

	// TODO 新增修改如果wechatReplyContent为空，则将id也值为空
	
	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	@Autowired
	WechatReplyClickService service;
	@Autowired
	WechatReplyContentService cService;

	/**
	 * 获取关键词信息(关注回复、默认回复)
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@MoreSerializeField({
		@SerializeField(clazz = WechatReplyClick.class, includes = { "id", "appId","replyType",
				"wechatReplyContent"}),
		@SerializeField(clazz = WechatReplyContent.class, includes = { "msgType", "content",
				"wechatMaterial"}),
		@SerializeField(clazz = WechatMaterial.class, includes = { "request","id"}),
	})
	public ResponseInfo get(@RequestParam Integer replyType,@RequestParam String appId) 
			throws GlobalException {
		return new ResponseInfo(service.findByReplyTypeAndAppId(replyType, appId));
	}

	/**
	 * 添加
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Validated(value = SaveClick.class) WechatReplyClick wechatReplyClick)
			throws GlobalException {
		this.initWechatReplyContent(wechatReplyClick);
		service.saveKeyWord(wechatReplyClick);
		return new ResponseInfo();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo update(@RequestBody @Validated(value = UpdateClick.class) WechatReplyClick wechatReplyClick)
			throws GlobalException {
		if (wechatReplyClick.getWechatReplyContent() != null) {
			this.initWechatReplyContent(wechatReplyClick);
		}
		service.updateKeyWord(wechatReplyClick);
		return new ResponseInfo();
	}

	/**
	 * 删除
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		WechatReplyClick click = service.findById(ids.getIds()[0]);
		if (click != null) {
			Integer contentId = click.getReplyContentId();
			if (contentId != null) {
				cService.delete(contentId);
			}
			service.delete(ids.getIds()[0]);
		}
		return new ResponseInfo();
	}

	/**
	 * 初始化wechatReplyContent
	 */
	private WechatReplyClick initWechatReplyContent(WechatReplyClick wechatReplyClick) throws GlobalException {
		WechatReplyContent content = new WechatReplyContent();
		String ruleName = "关注后自动回复";
		if (wechatReplyClick.getReplyType().equals(2)) {
			ruleName = "默认回复";
		}
		content.setRuleName(wechatReplyClick.getAppId() + ruleName);
		content.setAppId(wechatReplyClick.getAppId());
		content.setSortNum(10);
		WechatReplyContent oldContent = wechatReplyClick.getWechatReplyContent();
		content.setMediaId(oldContent.getMediaId());
		content.setContent(oldContent.getContent());
		content.setIsEnable(true);
		wechatReplyClick.setWechatReplyContent(content);
		return wechatReplyClick;
	}
	
}

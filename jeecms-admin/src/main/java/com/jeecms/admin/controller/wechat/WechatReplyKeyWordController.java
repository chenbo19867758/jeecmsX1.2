package com.jeecms.admin.controller.wechat;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.domain.WechatReplyKeyword;
import com.jeecms.wechat.service.WechatReplyKeywordService;

@RequestMapping(value = "/keyWord")
@RestController
public class WechatReplyKeyWordController extends BaseController<WechatReplyKeyword, Integer> {

	@Autowired
	private WechatReplyKeywordService service;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 添加关键词
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Valid WechatReplyKeyword replyKeyword,
			 BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		if (replyKeyword.getWechatReplyContent() != null) {
			WechatReplyContent content = replyKeyword.getWechatReplyContent();
			content.setAppId(replyKeyword.getAppId());
			replyKeyword.setWechatReplyContent(content);
		} else {
			return new ResponseInfo(RPCErrorCodeEnum.CONTENT_NOTNULL.getCode(),
					RPCErrorCodeEnum.CONTENT_NOTNULL.getDefaultMessage());
		}
		return service.saveKeyWork(replyKeyword);
	}

	/**
	 * 查询关键字列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = WechatReplyKeyword.class, includes = {"wordkeyEq", 
				"wordkeyLike","replyContentId","msgType"})
	})
	public ResponseInfo list(@RequestParam(value = "appId") String appId,
			@PageableDefault(sort = "sortNum", direction = Direction.ASC) Paginable paginable) 
					throws GlobalException {
		return new ResponseInfo(service.findByAppId(appId));
	}
}

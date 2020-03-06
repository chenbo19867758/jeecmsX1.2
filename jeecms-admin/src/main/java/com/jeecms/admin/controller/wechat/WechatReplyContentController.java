package com.jeecms.admin.controller.wechat;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.domain.WechatReplyKeyword;
import com.jeecms.wechat.service.WechatReplyClickService;
import com.jeecms.wechat.service.WechatReplyContentService;
import com.jeecms.wechat.service.WechatReplyKeywordService;

@RequestMapping("/wechatreplycontent")
@RestController
public class WechatReplyContentController extends BaseController<WechatReplyContent, Integer> {

	// TODO 列表中切记必须排除掉某些东西(事件)

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	@Autowired
	private WechatReplyContentService wechatReplyContentService;
	@Autowired
	private WechatReplyClickService wechatReplyClickService;
	@Autowired
	private WechatReplyKeywordService wechatReplyKeywordService;

	/**
	 * 关键字回复列表
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = WechatReplyKeyword.class, includes = { "wordkeyEq","wordkeyLike"}),
			@SerializeField(clazz = WechatReplyContent.class, includes = { "ruleName", "id","msgType",
					"content","wechatReplyKeywordList" }) })
	public ResponseInfo page(@RequestParam String appId,@RequestParam(required = false) String searchStr,
			@PageableDefault(sort = "sortNum", direction = Direction.ASC) Pageable pageable) 
					throws GlobalException {
		return new ResponseInfo(wechatReplyContentService.getPage(appId,pageable,searchStr));
	}

	/**
	 * 查询单个
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = WechatReplyContent.class, includes = { "id", "msgType","ruleName",
					"mediaId","content","title","description","musicUrl","hqMusicUrll",
					"thumbMediaId","isEnable","wechatReplyKeywordList","wechatMaterial","sortNum"}),
			@SerializeField(clazz = WechatMaterial.class, includes = { "mediaType", "request", 
					"requestArray","wechatUpdateTime" }),
			@SerializeField(clazz = WechatReplyKeyword.class, includes = { "wordkeyEq","wordkeyLike"})
	})
	public ResponseInfo get(HttpServletRequest request, @PathVariable(name = "id") Integer id) 
			throws GlobalException {
		// 如果传入的id错误返回空，如果传入的id是事件返回空
		WechatReplyContent content = wechatReplyContentService.getId(id);
		if (content != null) {
			List<Integer> ids = wechatReplyClickService.findByAppId(content.getAppId());
			if (ids.contains(id)) {
				return new ResponseInfo();
			}
			return new ResponseInfo(content);
		} else {
			return new ResponseInfo();
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo update(@RequestBody @Valid WechatReplyKeyword replyKeyword,BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		WechatReplyContent content = replyKeyword.getWechatReplyContent();
		if (content != null) {
			if (content.getId() != null) {
				content = wechatReplyContentService.findById(content.getId());
				wechatReplyKeywordService.delete(content.getWechatReplyKeywordList());
				wechatReplyContentService.delete(content);
				replyKeyword.getWechatReplyContent().setAppId(replyKeyword.getAppId());
				replyKeyword.getWechatReplyContent().setId(null);
				wechatReplyKeywordService.saveKeyWork(replyKeyword);
				return new ResponseInfo();
			}
		} 
		return new ResponseInfo();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dto,BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		WechatReplyContent content = wechatReplyContentService.findById(dto.getIds()[0]);
		if (content != null) {
			List<Integer> ids = wechatReplyClickService.findByAppId(content.getAppId());
			if (ids.contains(content.getId())) {
				return new ResponseInfo();
			}
			wechatReplyKeywordService.delete(content.getWechatReplyKeywordList());
			wechatReplyContentService.delete(content);
		}
		
		return new ResponseInfo();
	}

}

/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.Content;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.member.domain.MemberGroup;
import com.jeecms.member.domain.MemberLevel;
import com.jeecms.system.domain.SysBlackList;
import com.jeecms.system.service.SysBlackListService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 黑名单controller层
 * 
 * @author: chenming
 * @date: 2019年5月9日 下午5:43:16
 */
@RequestMapping("/sysblacklist")
@RestController
public class SysBlackListController extends BaseController<SysBlackList, Integer> {

	@Autowired
	private SysBlackListService service;

	/**
	 * 列表分页
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = SysBlackList.class, includes = { "id", "userId", "ip", "userName",
					"createUser", "createTime", "user", "userSiteName" }),
			@SerializeField(clazz = CoreUser.class, includes = { "integral", "userGroup", "userLevel" }),
			@SerializeField(clazz = MemberGroup.class, includes = { "groupName" }),
			@SerializeField(clazz = MemberLevel.class, includes = { "levelName" }) })
	public ResponseInfo page(HttpServletRequest request, Pageable pageable, @RequestParam boolean status,
			@RequestParam(required = false) String userName, @RequestParam(required = false) String ip)
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(service.getPage(status, userName, ip, siteId, pageable));
	}

	/**
	 * 获取当前黑名单用户的所有评论信息
	 */
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = SysBlackList.class, includes = { "id", "ip", "userName", 
					"createUser", "createTime", "userComments" }),
			@SerializeField(clazz = UserComment.class, includes = { "commentText", "createTime",
					"content" }),
			@SerializeField(clazz = Content.class, includes = { "channel", "title" }),
			@SerializeField(clazz = Channel.class, includes = { "name" }) })
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return new ResponseInfo(service.findCommentList(id));
	}

	/**
	 * 删除某个黑名单
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels, BindingResult result, HttpServletRequest request)
			throws GlobalException {
		super.validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		SysBlackList sysBlackList = service.findById(dels.getIds()[0]);
		if (!siteId.equals(sysBlackList.getSiteId())) {
			return new ResponseInfo(SettingErrorCodeEnum.FORBIDDEN_DATA_IS_NOT_SITE_DATA.getCode(),
					SettingErrorCodeEnum.FORBIDDEN_DATA_IS_NOT_SITE_DATA.getDefaultMessage());
		}
		return super.physicalDelete(dels.getIds()[0]);
	}
}

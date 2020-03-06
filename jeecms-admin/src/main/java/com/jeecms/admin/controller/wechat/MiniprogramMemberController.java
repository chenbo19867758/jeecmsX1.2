package com.jeecms.admin.controller.wechat;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.MiniprogramMember;
import com.jeecms.wechat.domain.MiniprogramMember.DeleteMember;
import com.jeecms.wechat.domain.MiniprogramMember.SaveMember;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.MiniprogramMemberService;

/**
 * 小程序成员管理controller层实现类
 * 
 * @author: chenming
 * @date: 2019年6月10日 上午10:03:44
 */
@RequestMapping("/miniprogrammember")
@RestController
public class MiniprogramMemberController extends BaseController<MiniprogramMember, Integer> {

	@Autowired
	private MiniprogramMemberService service;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 新增
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo addMember(@RequestBody @Validated(value = SaveMember.class) MiniprogramMember member,
			HttpServletRequest request) throws GlobalException {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, member.getAppId());
		if (service.isExist(member.getAppId(), member.getWechatId())) {
			return new ResponseInfo(RPCErrorCodeEnum.WECHAT_IS_ALREADY_BOUND_TO_THE_APPLET.getCode(),
					RPCErrorCodeEnum.WECHAT_IS_ALREADY_BOUND_TO_THE_APPLET.getDefaultMessage());
		} else {
			service.addMember(member.getAppId(), member.getWechatId());
			return new ResponseInfo(true);
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo deleteMember(
			@RequestBody @Validated(value = DeleteMember.class) MiniprogramMember miniprogramMember,
			BindingResult result, HttpServletRequest request) throws GlobalException {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null,
				miniprogramMember.getAppId());
		service.deleteMember(miniprogramMember);
		return new ResponseInfo(true);
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@SerializeField(clazz = MiniprogramMember.class, includes = { "id", "wechatId", "createTime", "appId" })
	public ResponseInfo findAllMember(@RequestParam String appId) throws GlobalException {
		return new ResponseInfo(service.getMember(appId));
	}
}
package com.jeecms.admin.controller.wechat;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.WechatMenu;
import com.jeecms.wechat.domain.WechatMenuGroup;
import com.jeecms.wechat.domain.WechatMenuGroup.SaveWechatMenuGroup;
import com.jeecms.wechat.domain.WechatMenuGroup.UpdateWechatMenuGroup;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatMenuGroupService;
import com.jeecms.wechat.service.WechatMenuService;

/**
 * 微信菜单管理
 * @author: chenming
 * @date:   2019年5月31日 上午10:17:58
 */
@RequestMapping("/wechatMenu")
@RestController
public class WechatMenuController extends BaseController<WechatMenu, Integer> {

	@Autowired
	private WechatMenuService wechatMenuService;
	@Autowired
	private WechatMenuGroupService wechatMenuGroupService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 新增一组菜单
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo saveMenu(@RequestBody @Validated(value = SaveWechatMenuGroup.class) WechatMenuGroup wGroup, 
			HttpServletRequest request, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, wGroup.getAppId());
		wechatMenuService.saveMenu(wGroup);
		return new ResponseInfo(true);
	}

	/**
	 * 修改某个菜单组的信息
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo updateMenu(
			@RequestBody @Validated(value = UpdateWechatMenuGroup.class) WechatMenuGroup wGroup, 
			HttpServletRequest request, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		WechatMenuGroup bean = wechatMenuGroupService.findById(wGroup.getId());
		// 判断如果是默认菜单，此菜单如果已经是生效而传入的status又是未生效直接抛出异常
		if (WechatMenuGroup.DEFAULT_MENU.equals(bean.getMenuGroupType())) {
			if (WechatMenuGroup.ACTIVE_STATUS.equals(bean.getStatus())) {
				if (WechatMenuGroup.NOT_ACTIVE_STATUS.equals(wGroup.getStatus())) {
					return new ResponseInfo(
						RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getCode(),
						RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getDefaultMessage());
				}
			}
		} else {
			// 如果需要修改的个性化菜单现在的状态是生效则直接抛出异常
			if (WechatMenuGroup.ACTIVE_STATUS.equals(bean.getStatus())) {
				return new ResponseInfo(RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getCode(),
						RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getDefaultMessage());
			}
		}
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, bean.getAppId());
		wGroup.setAppId(bean.getAppId());
		wGroup.setMenuGroupType(bean.getMenuGroupType());
		wechatMenuService.updateMenu(wGroup);
		return new ResponseInfo(true);
	}

}
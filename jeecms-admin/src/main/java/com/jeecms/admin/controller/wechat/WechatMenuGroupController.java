package com.jeecms.admin.controller.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.PageableUtil;
import com.jeecms.system.domain.Area;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatFansTag;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatMenu;
import com.jeecms.wechat.domain.WechatMenuGroup;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.domain.WechatReplyKeyword;
import com.jeecms.wechat.domain.dto.UpdateMenuStatusDto;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatMenuGroupService;
import com.jeecms.wechat.service.WechatMenuService;

@RequestMapping("/wechatmenugroup")
@RestController
public class WechatMenuGroupController extends BaseController<WechatMenuGroup, Integer> {

	/**
	 * TODO 此处可以考虑加一个接口
	 * 1. 微信端有一个接口删除默认菜单+所有个性化菜单
	 * 2. 可以考虑加一个接口，清空已生效的默认菜单+生效的个性化菜单
	 */
	
	
	@Autowired
	private WechatMenuGroupService wechatMenuGroupService;
	@Autowired
	private WechatMenuService wechatMenuService;
	@Autowired
	private AbstractWeChatInfoService wechatInfoService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	
	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 分页查询
	 */
	@RequestMapping(value = "/page",method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = WechatMenuGroup.class,includes = {"id","status","menuGroupName","countryName",
				"area","sexDictCode","clientDictCode","languageDictCode","wTag"}),
		@SerializeField(clazz = Area.class, includes = {"areaName"}),
		@SerializeField(clazz = WechatFansTag.class, includes = { "tagName" }) 
		})
	public ResponseInfo page(HttpServletRequest request, Pageable pageable,
			@RequestParam String  appId,
			@RequestParam String menuGroupType, 
			@RequestParam(value = "menuGroupName",required = false)String menuGroupName)
					throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<String> wechatIds = wechatInfoService.findWeChatInfo(AbstractWeChatInfo.TYPE_WECHAT, siteId)
				.stream().map(AbstractWeChatInfo::getAppId).collect(Collectors.toList());
		if (!wechatIds.contains(appId)) {
			return new ResponseInfo(RPCErrorCodeEnum.WECHAT_OR_APPLET_ARE_NOT_UNDER_THE_SITE.getCode(),
					RPCErrorCodeEnum.WECHAT_OR_APPLET_ARE_NOT_UNDER_THE_SITE.getDefaultMessage());
		}
		Map<String,String[]> params = new HashMap<String,String[]>(3);
		params.put("EQ_appId_String", new String[]{appId});
		params.put("EQ_menuGroupType_String", new String[]{menuGroupType});
		if (StringUtils.isNotBlank(menuGroupName)) {
			params.put("LIKE_menuGroupName_String", new String[] {menuGroupName});
		}
		List<Order> orders = new ArrayList<Sort.Order>();
		orders.add(new Order(Direction.ASC,"status"));
		orders.add(new Order(Direction.ASC,"createTime"));
		return new ResponseInfo(
				wechatMenuGroupService.getPage(params, PageableUtil.by(pageable, orders), false));
	}

	/**
	 * 删除操作，删除菜单组与相关联的WechatMenu中的数据
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result,HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		WechatMenuGroup wGroup = wechatMenuGroupService.findById(ids.getIds()[0]);
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, wGroup.getAppId());
		if (WechatMenuGroup.DEFAULT_MENU.equals(wGroup.getMenuGroupType())) {
			if (WechatMenuGroup.ACTIVE_STATUS.equals(wGroup.getStatus())) {
				return new ResponseInfo(
						RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getCode(),
						RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getDefaultMessage());
			}
		}
		return new ResponseInfo(wechatMenuGroupService.deleteMenuGroup(ids.getIds()[0]));
	}

	/**
	 * 获取单个菜单组数据
	 */
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = WechatMenuGroup.class, includes = { "id", "menuId", "appId", 
					"menuGroupType", "status", "menuGroupName", "area", "sexDictCode", "tagId", 
					"clientDictCode", "languageDictCode", "menuList" }),
			@SerializeField(clazz = WechatMenu.class, includes = { "id", "menuName", "menuType", 
					"menuKey", "wechatReplyContent", "linkUrl", "miniprogramAppid", "choiceType",
					"miniprogramPagepath", "material", "childs" }),
			@SerializeField(clazz = Area.class, includes = { "areaCode", "areaName", "parent" }),
			@SerializeField(clazz = WechatMaterial.class, includes = { "id", "appId", "mediaType", 
					"mediaId", "request", "requestArray" }),
			@SerializeField(clazz = WechatReplyContent.class, includes = { "id", "msgType", "title",
					"content", "ruleName", "sortNum", "mediaId",  "description", "musicUrl", 
					"hqMusicUrll", "thumbMediaId", "keyWord", "wechatMaterial" }),
			@SerializeField(clazz = WechatReplyKeyword.class, includes = { "wordkeyEq", "wordkeyLike" }) })
	@Override
	public ResponseInfo get(@PathVariable(name = "id") Integer id) throws GlobalException {
		return new ResponseInfo(wechatMenuGroupService.getMenuGroup(id));
	}

	/**
	 * 修改菜单组的状态
	 */
	@RequestMapping(value = "/status",method = RequestMethod.PUT)
	public ResponseInfo updateStatus(@RequestBody @Valid UpdateMenuStatusDto dto, BindingResult result,HttpServletRequest request) 
			throws GlobalException {
		super.validateBindingResult(result);
		WechatMenuGroup wGroup = wechatMenuGroupService.findById(dto.getId());
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, wGroup.getAppId());
		// 如果菜单状态是本来是开启但是修改成为未开启直接抛异常
		if (WechatMenuGroup.ACTIVE_STATUS.equals(wGroup.getStatus())
				&&
			WechatMenuGroup.NOT_ACTIVE_STATUS.equals(dto.getStatus())) {
			return new ResponseInfo(
					RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getCode(),
					RPCErrorCodeEnum.WECHAT_MENU_STATUS_IS_WRONG.getDefaultMessage());
		}
		wechatMenuService.updateStatus(dto);
		return new ResponseInfo(true);
	}

}

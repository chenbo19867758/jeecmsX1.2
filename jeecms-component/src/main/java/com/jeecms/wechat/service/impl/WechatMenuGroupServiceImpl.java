package com.jeecms.wechat.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.dao.WechatMenuGroupDao;
import com.jeecms.wechat.domain.WechatMenu;
import com.jeecms.wechat.domain.WechatMenuGroup;
import com.jeecms.wechat.service.WechatMenuGroupService;
import com.jeecms.wechat.service.WechatMenuService;

/**
 * 菜单组service实现类
 * @author: chenming
 * @date:   2019年6月12日 上午11:08:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatMenuGroupServiceImpl extends BaseServiceImpl<WechatMenuGroup, WechatMenuGroupDao, Integer>
		implements WechatMenuGroupService {

	@Autowired
	private WechatMenuService menuService;

	@Override
	public Boolean deleteMenuGroup(Integer id) throws GlobalException {
		WechatMenuGroup menuGroup = super.findById(id);
		super.physicalDelete(id);
		menuService.deleteMenu(menuGroup);
		return true;
	}

	@Override
	public void updateStatic(String appId) throws GlobalException {
		dao.updateStatus(appId);
	}

	@Override
	public WechatMenuGroup getMenuGroup(Integer id) throws GlobalException {
		WechatMenuGroup menuGroup = super.findById(id);
		List<WechatMenu> menuList = menuService.findByMenuGroupId(id);
		if (menuList != null && menuList.size() > 0) {
			// 此处加根据ID排序，防止之后进行遍历返回前台时数据时乱的
			menuList = menuList.stream().sorted(
					Comparator.comparing(WechatMenu::getId)).collect(Collectors.toList());
			JSONArray jsonArray = this.getMenu(menuList, null);
			List<WechatMenu> menus = jsonArray.toJavaList(WechatMenu.class);
			menuGroup.setMenuList(menus);
		}
		return menuGroup;
	}

	/**
	 * 菜单转树形结构
	 */
	public JSONArray getMenu(List<WechatMenu> lists, Integer pid) {
		JSONArray arrays = new JSONArray();
		for (WechatMenu a : lists) {
			JSONObject jsonObject = new JSONObject();
			if (a.getParentId() == pid
					|| 
				(a.getParentId() != null 
					&& 
					pid != null 
					&& 
					a.getParentId().intValue() == pid.intValue())) {
				jsonObject.put("id", a.getId());
				jsonObject.put("menuName", a.getMenuName());
				jsonObject.put("menuType", a.getMenuType());
				jsonObject.put("menuKey", a.getMenuKey());
				jsonObject.put("linkUrl", a.getLinkUrl());
				jsonObject.put("miniprogramAppid", a.getMiniprogramAppid());
				jsonObject.put("miniprogramPagepath", a.getMiniprogramPagepath());
				jsonObject.put("mediaId", a.getMediaId());
				jsonObject.put("material", a.getMaterial());
				jsonObject.put("wechatReplyContent", a.getWechatReplyContent());
				jsonObject.put("choiceType", a.getChoiceType());
				JSONArray childs = getMenu(lists, a.getId());
				if (childs.size() > 0) {
					jsonObject.put("childs", childs);
				}
				arrays.add(jsonObject);
			}
		}
		return arrays;
	}

	@Override
	public WechatMenuGroup updateStatus(Integer id) throws GlobalException {
		WechatMenuGroup wGroup = this.getMenuGroup(id);
		return wGroup;

	}

}
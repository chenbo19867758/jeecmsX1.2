package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.api.mp.ConditionalApiService;
import com.jeecms.common.wechat.api.mp.MenuApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.menu.AddConditionalRequest;
import com.jeecms.common.wechat.bean.request.mp.menu.CreateMenuRequest;
import com.jeecms.common.wechat.bean.request.mp.menu.DelConditionalRequest;
import com.jeecms.common.wechat.bean.request.mp.menu.common.ComplexButton;
import com.jeecms.common.wechat.bean.request.mp.menu.common.Matchrule;
import com.jeecms.common.wechat.bean.response.mp.menu.AddConditionalResponse;
import com.jeecms.system.domain.Area;
import com.jeecms.system.service.AreaService;
import com.jeecms.wechat.dao.WechatMenuDao;
import com.jeecms.wechat.domain.WechatMenu;
import com.jeecms.wechat.domain.WechatMenuGroup;
import com.jeecms.wechat.domain.dto.UpdateMenuStatusDto;
import com.jeecms.wechat.service.WechatMaterialService;
import com.jeecms.wechat.service.WechatMenuGroupService;
import com.jeecms.wechat.service.WechatMenuService;

/**
 * 微信菜单service实现类
 * @author: chenming
 * @date:   2019年5月31日 上午11:19:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatMenuServiceImpl extends BaseServiceImpl<WechatMenu, WechatMenuDao, Integer>
		implements WechatMenuService {

	private static final Integer MENUGROUP_STATUS_TRUE = 1;
	private static final Integer MENUGROUP_TYPE_INDIVIDUATION = 2;
	private static final String UNLIMITED_GENDER = "3";
	
	@Autowired
	private MenuApiService menuApiService;
	@Autowired
	private WechatMenuGroupService menuGroupService;
	@Autowired
	private ConditionalApiService conditionalApiService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private WechatMaterialService materialService;

	private ValidateToken getToken(String appId) {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(appId);
		return validateToken;
	}

	private void copyMenuToButton(WechatMenu menu, ComplexButton button) {
		button.setAppid(menu.getMiniprogramAppid());
		button.setKey(menu.getMenuKey() != null ? String.valueOf(menu.getMenuKey()) : null);
		button.setMediaId(null);
		if (WechatMenu.TYPE_MEDIAID.equals(menu.getMenuType())) {
			button.setMediaId(materialService.findById(menu.getMediaId()).getMediaId());
		} 
		button.setName(menu.getMenuName());
		button.setPagepath(menu.getMiniprogramPagepath());
		button.setType(menu.getMenuType());
		button.setUrl(menu.getLinkUrl());
	}

	@Override
	public Integer deleteMenu(WechatMenuGroup menuGroup) throws GlobalException {
		if (menuGroup.getMenuGroupType() == WechatMenuServiceImpl.MENUGROUP_TYPE_INDIVIDUATION
				&& menuGroup.getMenuId() != null) {
			ValidateToken validateToken = this.getToken(menuGroup.getAppId());
			conditionalApiService.delConditional(
					new DelConditionalRequest(menuGroup.getMenuId() + ""), validateToken);
		}
		return dao.deleteMenu(menuGroup.getId());
	}

	/**
	 * 新增菜单组
	 */
	@Override
	public WechatMenu saveMenu(WechatMenuGroup wGroup) throws GlobalException {
		ValidateToken validateToken = this.getToken(wGroup.getAppId());
		/*
		 * 判断是默认菜单还是个性化菜单，如果是默认菜单则需要关闭其它默认菜单，开启自身，如果是个性化菜单则进行其它操作
		 */
		if (wGroup.getMenuGroupType() == 1 && wGroup.getStatus() == 1) {
			menuGroupService.updateStatic(wGroup.getAppId());
		}
		WechatMenuGroup menuGroup = menuGroupService.save(wGroup);
		// 将数据转换成微信端数据，且将数据插入数据库
		List<ComplexButton> buttons = this.toButtons(wGroup, menuGroup);
		// 判断如上，如是默认菜单则直接发布到微信服务器，如果是个性化菜单则如上仅插入数据库，不上传如微信服务器
		if (wGroup.getMenuGroupType() == 1 && wGroup.getStatus() == 1) {
			CreateMenuRequest cMenuRequest = new CreateMenuRequest();
			cMenuRequest.setButton(buttons);
			menuApiService.createMenu(cMenuRequest, validateToken);
		}
		if (wGroup.getMenuGroupType() == WechatMenuServiceImpl.MENUGROUP_TYPE_INDIVIDUATION
				&& wGroup.getStatus() == WechatMenuServiceImpl.MENUGROUP_STATUS_TRUE) {

			Matchrule matchrule = this.getMatchrule(menuGroup);
			AddConditionalRequest aConditionalRequest = new AddConditionalRequest(buttons, matchrule);
			AddConditionalResponse aConditionalResponse = conditionalApiService.addConditional(
					aConditionalRequest, validateToken);
			menuGroup.setMenuId(Integer.valueOf(aConditionalResponse.getMenuid()));
			menuGroupService.update(menuGroup);
		}
		return null;
	}

	private Matchrule getMatchrule(WechatMenuGroup menuGroup) throws GlobalException {
		// 进行个性化菜单上传微信服务器的数据拼装
		String city = null;
		String province = null;
		if (menuGroup.getAreaCode() != null && menuGroup.getAreaCode().length() > 0) {
			Area area = areaService.findByAreaCode(menuGroup.getAreaCode()).get(0);
			switch (Integer.valueOf(area.getAreaDictCode())) {
				case 1:
					province = area.getAreaName();
//					province = province.substring(0, province.length() - 1);
					break;
				case 2:
					province = areaService.findById(area.getParentId()).getAreaName();
//					province = province.substring(0, province.length() - 1);
					city = area.getAreaName();
					city = city.substring(0, city.length() - 1);
					break;
				case 3:
					Area cityArea = areaService.findById(area.getParentId());
					province = areaService.findById(cityArea.getParentId()).getAreaName();
					// 在别的层级关系中，天津作为一个省，而后是天津市，再是区，三层结构，然后，微信的层级结构则是天津 区名
					// 地下特别表明的都市需要处理的，其它的都不需要处理
					if (province.equals("天津")|| province.equals("上海") || province.equals("重庆") || province.equals("北京") || (province.equals("海南") && cityArea.getAreaName().equals("省直辖县"))) {
						cityArea = areaService.findById(area.getId());
					}
//					province = province.substring(0, province.length() - 1);
					city = cityArea.getAreaName();
					city = city.substring(0, city.length() - 1);
					if (province.equals("澳门")) {
						city = "";
					}
					break;
				default:
					break;
			}
		}
		String sexDictCode = menuGroup.getSexDictCode();
		if (StringUtils.isBlank(sexDictCode) || WechatMenuServiceImpl.UNLIMITED_GENDER.equals(sexDictCode)) {
			menuGroup.setSexDictCode(null);
		}
		if (menuGroup.getClientDictCode() != null && menuGroup.getClientDictCode().length() == 0) {
			menuGroup.setClientDictCode(null);
		}
		if (menuGroup.getLanguageDictCode() != null && menuGroup.getLanguageDictCode().length() == 0) {
			menuGroup.setLanguageDictCode(null);
		}
		Matchrule matchrule = new Matchrule(menuGroup.getTagId() != null ? String.valueOf(menuGroup.getTagId()) : null, 
				menuGroup.getSexDictCode(), "中国", province, city, menuGroup.getClientDictCode(), 
				menuGroup.getLanguageDictCode());
		return matchrule;
	}

	/**
	 * 修改菜单组
	 */
	@Override
	public WechatMenu updateMenu(WechatMenuGroup wGroup) throws GlobalException {
		ValidateToken validateToken = this.getToken(wGroup.getAppId());
		this.deleteMenu(wGroup);
		List<ComplexButton> buttons = this.toButtons(wGroup, wGroup);
		wGroup = menuGroupService.updateAll(wGroup);
		CreateMenuRequest cMenuRequest = new CreateMenuRequest();
		cMenuRequest.setButton(buttons);
		/*
		 * 判断菜单类型，如果是默认菜单则说明有两种插入方式，如果此菜单是开启状态则将此菜单发布到微信服务端，
		 * 如果此菜单是关闭状态则只需修改本地数据库数据，如果是个性化菜单，则说明只有一种情况，则是此个性化菜
		 * 单无生效，则只需修改本地数据库数据，微信服务端数据无需任何处理
		 */
		if (wGroup.getMenuGroupType() == 1) {
			if (wGroup.getStatus() == 1) {
				menuApiService.createMenu(cMenuRequest, validateToken);
			}
		}
		return null;
	}

	/**
	 * 将数据转换成微信端数据，且将数据插入数据库
	 */
	private List<ComplexButton> toButtons(WechatMenuGroup oldGroup, WechatMenuGroup newGroup) 
			throws GlobalException {

		// 过滤第一层数据取出有子集的对象，拼接数据，且新增入数据库。遍历第二层，取出数据并且依次拼接数据
		List<WechatMenu> wList = oldGroup.getMenuList();
		// 一级菜单
		List<WechatMenu> wListParent = new ArrayList<WechatMenu>();
		List<ComplexButton> buttons = new ArrayList<ComplexButton>();
		// 遍历一级菜单
		for (WechatMenu wechatMenu : wList) {
			ComplexButton button = new ComplexButton();
			copyMenuToButton(wechatMenu, button);
			wechatMenu.setMenuGroupId(newGroup.getId());
			buttons.add(button);
			wListParent.add(wechatMenu);
		}
		// 批量插入一级菜单
		super.saveAll(wListParent);
		if (wListParent.size() > 0) {
			// 存储所有子菜单数据
			List<WechatMenu> subMenus = new ArrayList<WechatMenu>();
			// 遍历有子集的一级菜单
			int i = 0;
			for (WechatMenu wechatMenu : wListParent) {
				// 一级菜单下的二级菜单集合
				List<WechatMenu> wechatMenuPart = wechatMenu.getChilds();
				List<ComplexButton> subButtons = new ArrayList<ComplexButton>();
				if (wechatMenuPart == null) {
					i++;
					continue;
				}
				for (WechatMenu wMenu : wechatMenuPart) {
					ComplexButton subButton = new ComplexButton();
					copyMenuToButton(wMenu, subButton);
					subButtons.add(subButton);
					wMenu.setMenuGroupId(newGroup.getId());
					wMenu.setParentId(wechatMenu.getId());
					wMenu.setParent(wechatMenu);
					subMenus.add(wMenu);
				}
				buttons.get(i).setSubButton(subButtons);
				i++;
			}
			// 批量保存所有子菜单
			super.saveAll(subMenus);
		}
		return buttons;
	}

	/**
	 * 修改菜单组状态
	 */
	@Override
	public WechatMenu updateStatus(UpdateMenuStatusDto dto) throws GlobalException {
		WechatMenuGroup wGroup = menuGroupService.getMenuGroup(dto.getId());
		String appId = wGroup.getAppId();
		Integer status = dto.getStatus();
		ValidateToken validateToken = this.getToken(wGroup.getAppId());
		/*
		 * 判断菜单类型，如果是默认菜单则只有一种操作行为，关闭其它菜单，开启这个菜单，如果是个性化菜单， 则根据status进行不同的行为操作
		 */
		if (wGroup.getMenuGroupType() == 1) {
			menuGroupService.updateStatic(appId);
			wGroup.setStatus(1);
		} else {
			wGroup.setStatus(status);
		}
		// 修改菜单组，并且获取到其对象。
		WechatMenuGroup menuGroup = menuGroupService.update(wGroup);
		List<ComplexButton> buttons = this.toButtons(wGroup, menuGroup);
		// 判断如上，如果是默认菜单则直接上传入微信服务器，如是个性化菜单则进行判断操作
		if (wGroup.getMenuGroupType() == 1) {
			CreateMenuRequest cMenuRequest = new CreateMenuRequest();
			cMenuRequest.setButton(buttons);
			menuApiService.createMenu(cMenuRequest, validateToken);
		} else {
			Matchrule matchrule = this.getMatchrule(menuGroup);
			AddConditionalRequest aConditionalRequest = new AddConditionalRequest(buttons, matchrule);
			/*
			 * 判断个性化菜单状态，如果是将关闭的状态打开则将个性化菜单数据上传入微信服务端，如果是将打开的状态关闭，
			 * 则将个性化菜单微信服务端的数据删除，本地数据库的数据不进行任何处理。
			 */
			if (status == 1) {
				AddConditionalResponse aConditionalResponse = conditionalApiService.addConditional(
						aConditionalRequest,validateToken);
				menuGroup.setMenuId(Integer.valueOf(aConditionalResponse.getMenuid()));
				menuGroupService.update(menuGroup);
			} else {
				DelConditionalRequest dRequest = new DelConditionalRequest(menuGroup.getMenuId() + "");
				conditionalApiService.delConditional(dRequest, validateToken);
			}
		}
		return null;
	}

	@Override
	public List<WechatMenu> findByMenuGroupId(Integer menuGroupId) throws GlobalException {
		return dao.findByMenuGroupId(menuGroupId);
	}

}
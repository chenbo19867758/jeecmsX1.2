package com.jeecms.admin.controller.auth;

import com.jeecms.auth.domain.CoreApi;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.dto.CoreMenuDto;
import com.jeecms.auth.domain.vo.MenuVO;
import com.jeecms.auth.domain.vo.SortMenuVO;
import com.jeecms.auth.service.CoreMenuService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.controller.BaseTreeController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.SortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.service.CmsOrgService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 菜单管理
 *
 * @author: chenming
 * @date: 2019年4月9日 下午3:03:43
 */
@RequestMapping(value = "/menus")
@RestController
public class CoreMenuController extends BaseTreeController<CoreMenu, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 查询所有(树形结构)
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param paginable
	 *            Paginable 分页组件
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	@SerializeField(clazz = CoreMenu.class, includes = { "id", "path", "name", "menuName", "hidden", "menuType",
			"children", "sortNum", "isAuth" })
	public ResponseInfo tree(HttpServletRequest request, Paginable paginable) {
		paginable.setSort(new Sort(Direction.ASC, "sortNum"));
		List<CoreMenu> menuList = service.getList(super.getCommonParams(request), paginable, true);
		return new ResponseInfo(super.getTree(menuList, null));
	}

	/**
	 * 查询所有（菜单和权限分开存放）
	 *
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/getMenus", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = MenuVO.class, includes = { "needChangePassword", "nextNeedCaptcha", "lastLoginTime",
					"userName", "lastLoginIP", "menus", "perms" }),
			@SerializeField(clazz = SortMenuVO.class, includes = { "id", "path", "name", "menuName", "hidden", "icon",
					"children", "component", "sortNum", "redirect" }) })
	public ResponseInfo getMenus(HttpServletRequest request) throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(request);
		return new ResponseInfo(service.getMenu(user));
	}

	/**
	 * 保存排序
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	@Override
	public ResponseInfo sort(@RequestBody(required = false) @Valid SortDto sort, BindingResult result)
			throws GlobalException {
		super.checkServerMode();
		return super.sort(sort, result);
	}

	/**
	 * 删除
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result) throws GlobalException {
		super.checkServerMode();
		super.validateBindingResult(result);
		service.remove(ids.getIds()[0]);
		return new ResponseInfo();
	}

	/**
	 * 详细信息
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "icon", "menuName", "name", "menuType", "path",
					"redirect", "sortNum", "component", "apis", "parentIds", "hidden", "isAuth" }),
			@SerializeField(clazz = CoreApi.class, includes = { "id", "apiName", "apiUrl" }) })
	public ResponseInfo get(@PathVariable(name = "id") Integer id) throws GlobalException {
		CoreMenu menu = service.get(id);
		List<Integer> list = new ArrayList<Integer>();
		// 获取到的是从父级往上开始获取，所以需要反转
		list = getParentIds(list, menu.getParentId());
		Collections.reverse(list);
		menu.setParentIds(list.toArray(new Integer[list.size()]));
		return new ResponseInfo(menu);
	}

	/**
	 * 保存
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo saveChild(@RequestBody @Valid CoreMenuDto menuDto, BindingResult result)
			throws GlobalException {
		super.checkServerMode();
		super.validateBindingResult(result);
		if (menuDto.getName() != null && StringUtils.isNotBlank(menuDto.getName())) {
			Boolean routingResult = this.ckRouting(menuDto.getName(), null);
			if (!routingResult) {
				return new ResponseInfo(SettingErrorCodeEnum.ROUTING_ALREADY_EXIST.getCode(),
						SettingErrorCodeEnum.ROUTING_ALREADY_EXIST.getDefaultMessage());
			}
		}
		if (menuDto.getPath() != null && !this.ckPath(menuDto.getPath(), null)) {
			return new ResponseInfo(SettingErrorCodeEnum.ROUTING_ADDRESS_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.ROUTING_ADDRESS_ALREADY_EXIST.getDefaultMessage());
		}
		service.save(menuDto);
		return new ResponseInfo();
	}

	/**
	 * 更新
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo update(@RequestBody @Valid CoreMenuDto menuDto, BindingResult result) throws GlobalException {
		super.checkServerMode();
		super.validateBindingResult(result);
		if (StringUtils.isNotBlank(menuDto.getName())) {
			Boolean routingResult = this.ckRouting(menuDto.getName(), menuDto.getId());
			if (!routingResult) {
				return new ResponseInfo(SettingErrorCodeEnum.ROUTING_ALREADY_EXIST.getCode(),
						SettingErrorCodeEnum.ROUTING_ALREADY_EXIST.getDefaultMessage());
			}
		}
		if (menuDto.getPath() != null && !this.ckPath(menuDto.getPath(), menuDto.getId())) {
			return new ResponseInfo(SettingErrorCodeEnum.ROUTING_ADDRESS_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.ROUTING_ADDRESS_ALREADY_EXIST.getDefaultMessage());
		}
		service.update(menuDto);
		return new ResponseInfo();
	}

	/**
	 * 检查菜单地址是否唯一
	 *
	 * @param id
	 *            菜单id
	 * @param routing
	 *            路由标识
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/routes/unique", method = RequestMethod.GET)
	public ResponseInfo checkPath(@RequestParam(name = "id", required = false) Integer id, @RequestParam String routing)
			throws GlobalException {
		Boolean result = this.ckPath(routing, id);
		return new ResponseInfo(result);
	}

	/**
	 * 检查菜单标识是否唯一
	 *
	 * @param id
	 *            菜单id
	 * @param name
	 *            路由标识
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/name/unique", method = RequestMethod.GET)
	public ResponseInfo checkRouting(@RequestParam(name = "id", required = false) Integer id, @RequestParam String name)
			throws GlobalException {
		Boolean result = this.ckRouting(name, id);
		return new ResponseInfo(result);
	}

	/**
	 * 显示
	 */
	@RequestMapping(value = "/display", method = RequestMethod.PUT)
	public ResponseInfo enable(@RequestBody @Valid DeleteDto bean, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		service.show(bean.getIds()[0]);
		return new ResponseInfo();
	}

	/**
	 * 隐藏
	 */
	@RequestMapping(value = "/hidden", method = RequestMethod.PUT)
	public ResponseInfo disable(@RequestBody @Valid DeleteDto bean, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		service.hide(bean.getIds()[0]);
		return new ResponseInfo();
	}

	/**
	 * 开启参与权限分配
	 */
	@RequestMapping(value = "/openAuth", method = RequestMethod.PUT)
	public ResponseInfo openAuth(@RequestBody @Valid DeleteDto bean, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		/** 所有子菜单中级联选中 */
		Integer[] ids = bean.getIds();
		List<Integer> toOpenIds = new ArrayList<Integer>();
		for (Integer id : ids) {
			toOpenIds.add(id);
			CoreMenu menu = service.findById(id);
			toOpenIds.addAll(CoreMenu.fetchIds(menu.getChildren()));
		}
		service.openAuth(toOpenIds);
		/** 可能组织会需要调整权限，这里主动清空组织权限缓存 */
		orgService.clearAllOrgCache();
		return new ResponseInfo();
	}

	/**
	 * 关闭参与权限分配
	 */
	@RequestMapping(value = "/closeAuth", method = RequestMethod.PUT)
	public ResponseInfo closeAuth(@RequestBody @Valid DeleteDto bean, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		/** 所有子菜单中级联选中 */
		Integer[] ids = bean.getIds();
		List<Integer> toCloseIds = new ArrayList<Integer>();
		for (Integer id : ids) {
			toCloseIds.add(id);
			CoreMenu menu = service.findById(id);
			toCloseIds.addAll(CoreMenu.fetchIds(menu.getChildren()));
		}
		service.closeAuth(toCloseIds);
		/** 可能组织会需要调整权限，这里主动清空组织权限缓存 */
		orgService.clearAllOrgCache();
		return new ResponseInfo();
	}

	/**
	 * 判断菜单标识是否唯一
	 *
	 * @param menuId
	 *            菜单id
	 * @param routing
	 *            路由标识
	 * @return Boolean true 唯一 false 不唯一
	 */
	private Boolean ckRouting(String routing, Integer menuId) throws GlobalException {
		if (StringUtils.isBlank(routing)) {
			return true;
		}
		CoreMenu routingMenu = service.findByRouting(routing);
		if (menuId == null) {
			return routingMenu == null;
		}
		CoreMenu coreMenu = service.findById(menuId);
		if (routingMenu != null && coreMenu != null && !routing.equals(coreMenu.getName())) {
			return false;
		}
		return true;
	}

	/**
	 * 判断路由地址是否唯一
	 *
	 * @param menuId
	 *            菜单id
	 * @param path
	 *            路由地址
	 * @return Boolean true 唯一 false 不唯一
	 */
	private Boolean ckPath(String path, Integer menuId) {
		if (StringUtils.isBlank(path)) {
			return true;
		}
		CoreMenu routingMenu = service.findByPathAndHasDeleted(path);
		if (menuId == null) {
			return routingMenu == null;
		}
		CoreMenu coreMenu = service.findById(menuId);
		if (routingMenu != null && coreMenu != null && !path.equals(coreMenu.getPath())) {
			return false;
		}
		return true;
	}

	/**
	 * 获取菜单的所有上级菜单id
	 *
	 * @param list
	 *            保存获取到的list
	 * @param parentId
	 *            父级id
	 * @return list
	 * @throws GlobalException
	 */
	private List<Integer> getParentIds(List<Integer> list, Integer parentId) throws GlobalException {
		if (parentId != null) {
			list.add(parentId);
			CoreMenu menu = service.get(parentId);
			getParentIds(list, menu.getParentId());
		}
		return list;
	}

	@Autowired
	private CoreMenuService service;
	@Autowired
	private CmsOrgService orgService;
}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.auth;

import com.alibaba.fastjson.JSONArray;
import com.jeecms.admin.controller.BaseTreeAdminController;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.domain.dto.CmsOrgAgent;
import com.jeecms.auth.domain.dto.UserManagerDto;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.domain.dto.BeatchDto;
import com.jeecms.system.domain.dto.CmsOrgDto;
import com.jeecms.system.service.CmsOrgService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 组织管理控制器
 * 
 * @author: ljw
 * @date: 2019年4月15日 上午9:07:25
 */
@RequestMapping(value = "/orgs")
@RestController
public class CmsOrgController extends BaseTreeAdminController<CmsOrg, Integer> {

	@Autowired
	private CmsOrgService cmsOrgService;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private GlobalConfigService globalConfigService;

	/**
	 * 查询所有，得到树形结构（没有权限控制）
	 */
	@GetMapping(value = "/tree/noauth")
	public ResponseInfo noauth(Integer parentId, HttpServletRequest request, Paginable paginable)
			throws GlobalException {
		// 获取全部组织
		List<CmsOrg> orgs = cmsOrgService.getList(null, paginable, true);
		orgs = CmsOrgAgent.sortListBySortAndChild(orgs);
		JSONArray responseData = null;
		responseData = super.getChildTree(orgs, false, "name", "managerAble");
		return new ResponseInfo(responseData);
	}

	/**
	 * 查用户所属组织以及下属组织
	 * 
	 * @Title: findAll
	 * @param parentId
	 *            父组织
	 * @param permIgnore
	 *            是否忽略比较权限大小 （列表查询不比较 传true，新建和修改比较 传false）
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/tree")
	public ResponseInfo findAll(Integer parentId, Boolean permIgnore, HttpServletRequest request)
			throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(request);
		// 获取可分配权限组织
		if (permIgnore == null) {
			permIgnore = true;
		}
		List<CmsOrg> orgs = user.getAssignOrgsContainTop(permIgnore);
		orgs = CmsOrgAgent.sortListBySortAndChild(orgs);
		JSONArray responseData = null;
		responseData = super.getChildTree(orgs, false, "name", "managerAble");
		return new ResponseInfo(responseData);
	}

	/**
	 * 组织列表
	 * 
	 * @param: isVirtual
	 * @param: key
	 * @param: parentId
	 * @return: ResponseInfo
	 */
	@GetMapping("/list")
	public ResponseInfo page(HttpServletRequest request, @RequestParam(name = "isVirtual") Boolean isVirtual,
			@RequestParam("key") String key) throws GlobalException {
		List<CmsOrg> orgs = new ArrayList<CmsOrg>(10);
		CoreUser user = SystemContextUtils.getUser(request);
		GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
		// 判断组织是否对所有用户可见
		if (attr.getIsVisible()) {
			orgs = cmsOrgService.cmsList(isVirtual, key, null);
		} else {
			orgs = cmsOrgService.cmsList(isVirtual, key, user.getChildOrgIds());
		}
		JSONArray responseData = null;
		responseData = super.getChildTree(orgs, false, "createTime", "createUser", "deleteAble", "isVirtual",
				"managerAble", "name", "notCurrUserOrg", "orgLeader", "orgNum", "createTimes", "phone");
		return new ResponseInfo(responseData);
	}

	/**
	 * 获取详情
	 * 
	 * @param: @param
	 *             id
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@SerializeField(clazz = CmsOrg.class, includes = { "id", "name", "orgNum", "orgLeader", "phone", "orgFax",
			"orgRemark", "parentId", "parent", "createTime", "createTimes", "createUser", "isVirtual", "nodeIds",
			"managerAble", "deleteAble", "parentAble" })
	public ResponseInfo get(@NotNull @PathVariable(value = "id") Integer id, HttpServletRequest request)
			throws GlobalException {
		CmsOrg org = cmsOrgService.findById(id);
		return new ResponseInfo(org);
	}

	/**
	 * 添加组织
	 * 
	 * @param: @param
	 *             result
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping()
	@SerializeField(clazz = CmsOrg.class, includes = { "id" })
	public ResponseInfo save(@RequestBody @Valid CmsOrg cmsOrg, BindingResult result, HttpServletRequest request)
			throws GlobalException {
		Boolean flag = cmsOrgService.validName(cmsOrg.getName(), null);
		if (!flag) {
			return new ResponseInfo(UserErrorCodeEnum.ORGNAME_ALREADY_EXIST.getCode(),
					UserErrorCodeEnum.ORGNAME_ALREADY_EXIST.getDefaultMessage(), false);
		}
		cmsOrg = cmsOrgService.save(cmsOrg);
		return new ResponseInfo(cmsOrg);
	}

	/**
	 * 修改 组织
	 * 
	 * @param: @param
	 *             result
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	public ResponseInfo update(HttpServletRequest request, @RequestBody @Valid CmsOrgDto dto) throws GlobalException {
		Boolean flag = cmsOrgService.validName(dto.getName(), dto.getId());
		if (!flag) {
			return new ResponseInfo(UserErrorCodeEnum.ORGNAME_CANNOT_EMPTY.getCode(),
					UserErrorCodeEnum.ORGNAME_CANNOT_EMPTY.getDefaultMessage(), false);
		}
		CmsOrg bean = cmsOrgService.findById(dto.getId());
		bean.validManagerAble();
		bean = dto.dto(bean, dto);
		bean = cmsOrgService.update(bean);
		return new ResponseInfo();
	}

	/**
	 * 批量删除组织，根据可操作数据权限，系统设置中组织对所有用户可见， 存在别的组织不可操作，需要判断；
	 * 
	 * @param: @param
	 *             dto
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping
	public ResponseInfo delete(@RequestBody @Valid BeatchDto dto, BindingResult result, HttpServletRequest request)
			throws GlobalException {
		super.validateBindingResult(result);
		CoreUser user = SystemContextUtils.getCoreUser();
		return cmsOrgService.deleteOrg(dto, user.getChildOrgIds());
	}

	/**
	 * 成员管理
	 * 
	 * @Title: userList
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/list/users")
	@MoreSerializeField({
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "telephone", "email", "userExt",
					"managerAble", "notCurrUser", "deleteAble" }),
			@SerializeField(clazz = CoreUserExt.class, includes = { "realname", "linephone" }), })
	public ResponseInfo userList(HttpServletRequest request, Pageable pageable, Integer id) throws GlobalException {
		CmsOrg org = super.service.findById(id);
		org.validManagerAble();
		return new ResponseInfo(coreUserService.pageUser(true, Arrays.asList(id), null, null, true,
				CoreUser.AUDIT_USER_STATUS_PASS, null, null, null, null, pageable));
	}

	/**
	 * 移除成员
	 * 
	 * @Title: userList
	 * @param: id
	 *             组织ID
	 * @return: ResponseInfo
	 */
	@DeleteMapping(value = "/list/users")
	public ResponseInfo remove(@RequestBody UserManagerDto dto) throws GlobalException {
		dto.setType(UserManagerDto.ORG_TYPE);
		CmsOrg org = super.service.findById(dto.getId());
		org.validManagerAble();
		return coreUserService.removeUser(dto);
	}

	/**
	 * 添加成员
	 * 
	 * @Title: userList
	 * @param: id
	 *             组织ID
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/list/users")
	public ResponseInfo add(@RequestBody UserManagerDto dto) throws GlobalException {
		dto.setType(UserManagerDto.ORG_TYPE);
		CmsOrg org = super.service.findById(dto.getId());
		org.validManagerAble();
		return coreUserService.addUser(dto);
	}

	/**
	 * 验证组织名
	 * 
	 * @param: validName
	 * @return
	 */
	@RequestMapping(value = "/orgname/unique", method = RequestMethod.GET)
	public ResponseInfo username(@NotNull String orgname, Integer id) throws GlobalException {
		Boolean flag = cmsOrgService.validName(orgname, id);
		return new ResponseInfo(flag);
	}

	/**
	 * 组织排序
	 * 
	 * @Title: sort
	 * @param orgId
	 *            排序站点ID
	 * @param orderOrgId
	 *            被排序站点ID
	 * @param up
	 *            true上false下
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@GetMapping(value = "/sort")
	public ResponseInfo sort(HttpServletRequest request,
							 Integer orgId, Integer orderOrgId, Boolean up) throws GlobalException {
		List<CmsOrg> orgs = new ArrayList<CmsOrg>(10);
		CoreUser user = SystemContextUtils.getUser(request);
		GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
		// 判断组织是否对所有用户可见
		if (attr.getIsVisible()) {
			orgs = cmsOrgService.cmsList(null, null, null);
		} else {
			orgs = cmsOrgService.cmsList(null, null, user.getChildOrgIds());
		}
		DragSortDto dto = new DragSortDto();
		//true向上
		if (up) {
			dto.setFromId(orderOrgId);
			dto.setNextId(orgId);
		} else {
			//false向下
			dto.setFromId(orgId);
			dto.setNextId(orderOrgId);
		}
		return cmsOrgService.sort(dto, orgs);
	}

}

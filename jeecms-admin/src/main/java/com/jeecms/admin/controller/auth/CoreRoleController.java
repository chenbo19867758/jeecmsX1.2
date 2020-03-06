/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.jeecms.admin.controller.BaseAdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.domain.dto.UserManagerDto;
import com.jeecms.auth.service.CoreRoleService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.dto.BeatchDto;
import com.jeecms.util.SystemContextUtils;

/**
 * 角色管理控制器
 * @Description: 角色管理控制器
 * @author: ljw
 * @date: 2019年4月9日 上午10:38:21
 */	

@RestController
@RequestMapping("/roles")
public class CoreRoleController extends BaseAdminController<CoreRole, Integer> {

	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private CoreRoleService coreRoleService;

	@PostConstruct
	public void init() {
		String[] queryParams = { "roleName_LIKE" };
		super.setQueryParams(queryParams);
	}

	/**
	 * 条件查询列表(分页)
	 * 
	 * @param: request
	 * @param: response
	 * @param: pageable
	 * @return
	 */
	@GetMapping("/page")
	@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", "orgName",  
			"createTime", "createUser","notCurrUserRole","deleteAble","managerAble",   })
	public ResponseInfo page(@RequestParam("orgid") Integer orgid, @RequestParam("roleName") String roleName,
			@PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request) throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(request);
		List<Integer> ids = new ArrayList<Integer>(10);
		// 限制只能看到当前组织及以下组织
		if (orgid != null) {
			ids.add(orgid);
		} else {
			ids = user.getChildOrgIds();
		}
		return coreRoleService.pageRole(ids, roleName, pageable);
	}

	/**
	 * 角色列表
	 * 
	 * @param: request
	 * @param: response
	 * @param: pageable
	 * @return
	 */
	@GetMapping("/list")
	@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", 
			"notCurrUserRole","deleteAble","managerAble", })
	public ResponseInfo list(HttpServletRequest request, Integer orgId, String roleName)
			throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(request);
		List<Integer> ids = new ArrayList<Integer>();
		//组织ID为空，为当前登录用户所属组织下的全部角色
		if (orgId != null) {
			ids.add(orgId);
		} else {
			// 限制只能看到当前组织及以下组织
			ids = user.getChildOrgIds();
		}
		return new ResponseInfo(coreRoleService.listRole(ids, roleName));
	}

	/**
	 * 获取单个角色信息
	 * 
	 * @param: request
	 * @param: response
	 * @param: pageable
	 * @return
	 */
	@GetMapping("/{id}")
	@MoreSerializeField({ 
		@SerializeField(clazz = CoreRole.class, includes = { "id", "orgName", "description",
			"createTime", "createUser", "orgid", "roleName", "orgCode", "org",
			"managerAble","notCurrUserRole","deleteAble" }),
		@SerializeField(clazz = CmsOrg.class, includes = { "id","nodeIds" }),
	})
	public ResponseInfo get(@NotNull @PathVariable(value = "id") Integer id, HttpServletRequest request)
			throws GlobalException {
		CoreRole role =  super.service.findById(id);
		return new ResponseInfo(role);
	}

	/**
	 * 批量删除角色，批量删除组织，根据可操作数据权限，系统设置中组织对所有用户可见， 存在别的组织角色不可操作，需要判断；
	 * 
	 * @param: request
	 * @param: response
	 * @param: pageable
	 * @return
	 */
	@DeleteMapping
	public ResponseInfo deleteRole(@RequestBody @Valid BeatchDto dto, BindingResult result, 
			HttpServletRequest request)
			throws GlobalException {
		super.validateBindingResult(result);
		CoreUser user = SystemContextUtils.getUser(request);
		return coreRoleService.deleteBatch(dto, user.getOrgId());
	}

	/**
	 * 添加角色信息
	 * 
	 * @param: coreUserDto
	 * @param: result
	 * @return
	 */
	@Override
	@PostMapping()
	@MoreSerializeField({ 
		@SerializeField(clazz = CoreRole.class, includes = { "id"})
	})
	public ResponseInfo save(@RequestBody @Valid CoreRole coreRole, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		coreRole = coreRoleService.saveRole(coreRole);
		return new ResponseInfo(coreRole);
	}

	/**
	 * 更新角色
	 * 
	 * @Title: update
	 * @param: request
	 * @param: coreRole
	 * @param: result
	 * @return: ResponseInfo
	 */
	@PutMapping
	public ResponseInfo update(HttpServletRequest request, @RequestBody @Valid CoreRole coreRole, 
			BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		coreRoleService.updateRole(coreRole);
		return new ResponseInfo();
	}

	/**
	 * 成员管理
	 * 
	 * @Title: userList
	 * @param id
	 *            角色ID
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/list/users")
	@MoreSerializeField({ @SerializeField(clazz = CoreRole.class, includes = { "users" }),
			@SerializeField(clazz = CoreUser.class, 
			includes = { "id", "username", "telephone", "email", "userExt",
					"managerAble", "notCurrUser", "deleteAble"}),
			@SerializeField(clazz = CoreUserExt.class, includes = { "realname", "linephone" }), })
	public ResponseInfo userList(@NotNull Integer id, Pageable pageable, HttpServletRequest request)
			throws GlobalException {
		CoreRole role = super.service.findById(id);
		role.validManagerAble();
		List<Integer> roleids = Arrays.asList(id);
		return new ResponseInfo(coreUserService.pageUser(true, null, roleids, null, true, 
				CoreUser.AUDIT_USER_STATUS_PASS,
				null, null, null, null,pageable));
	}

	/**
	 * 移除成员
	 * 
	 * @Title: userList
	 * @param: id
	 *             角色ID
	 * @return: ResponseInfo
	 */
	@DeleteMapping(value = "/list/users")
	public ResponseInfo remove(@RequestBody UserManagerDto dto) throws GlobalException {
		dto.setType(UserManagerDto.ROLE_TYPE);
		CoreRole role = super.service.findById(dto.getId());
		role.validManagerAble();
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
		dto.setType(UserManagerDto.ROLE_TYPE);
		CoreRole role = super.service.findById(dto.getId());
		role.validManagerAble();
		return coreUserService.addUser(dto);
	}

}

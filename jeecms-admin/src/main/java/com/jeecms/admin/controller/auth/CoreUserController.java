/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.domain.dto.CoreUserDto;
import com.jeecms.auth.domain.dto.CoreUserDto.One;
import com.jeecms.auth.domain.dto.CoreUserDto.Three;
import com.jeecms.auth.domain.dto.CoreUserDto.Two;
import com.jeecms.auth.domain.dto.PswDto;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.auth.service.PasswdService;
import com.jeecms.common.base.domain.ThirdPartyResultDTO;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.DesUtil;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.domain.dto.BeatchDto;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.service.AbstractWeChatInfoService;

/**
 * 管理员控制器
 * 
 * @author: ljw
 * @date: 2019年4月9日
 * 
 */
@RestController
@RequestMapping("/users")
public class CoreUserController extends BaseAdminController<CoreUser, Integer> {

	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private GlobalConfigService globalConfigService;
	@Autowired
	private AbstractWeChatInfoService weChatInfoService;
	@Autowired
	private PasswdService passwdService;

	/**
	 * 条件查询列表
	 * 
	 * @Title: pageUser
	 * @param: key:用户名/邮箱/电话/真名/座机
	 * @return: ResponseInfo
	 */
	@GetMapping("/page")
	@MoreSerializeField({
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "enabled", "createTime", "itself",
					"createUser", "roleNames", "orgName", "managerAble", "userExt", "notCurrUser", "deleteAble" }),
			@SerializeField(clazz = CoreUserExt.class, includes = { "realname" }), })
	public ResponseInfo pageUser(HttpServletRequest request, Boolean enabled, String key, Integer orgid, Integer roleid,
			Integer userSecretId, Pageable pageable)
					throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(request);
		List<Integer> orgids = new ArrayList<Integer>();
		List<Integer> roleids = new ArrayList<Integer>();
		if (orgid != null) {
			orgids.add(orgid);
		} else {
			CmsOrg org = user.getOrg();
			orgids = Arrays.asList(org.getChildNodeIds());
		}
		if (roleid != null) {
			roleids.add(roleid);
		}
		return new ResponseInfo(coreUserService.pageUser(enabled, orgids, roleids, key, true,
				CoreUser.AUDIT_USER_STATUS_PASS, null, null, userSecretId, null, pageable));
	}

	/**
	 * 管理员列表
	 * 
	 * @Title: pageUser
	 * @param: key:用户名/邮箱/电话/真名/座机
	 * @return: ResponseInfo
	 */
	@GetMapping("/list")
	@MoreSerializeField({ @SerializeField(clazz = CoreUser.class, includes = { "id", "username", "managerAble",
			"notCurrUser", "deleteAble" }), })
	public ResponseInfo list(HttpServletRequest request) throws GlobalException {
		Map<String, String[]> map = new HashMap<String, String[]>(3);
		map.put("EQ_admin_Boolean", new String[] { "true" });
		return new ResponseInfo(service.getList(map, null, true));
	}

	/**
	 * 管理员添加
	 * 
	 * @Title: saveUser
	 * @return: ResponseInfo
	 */
	@PostMapping
	@MoreSerializeField({ @SerializeField(clazz = CoreUser.class, includes = { "id" }) })
	public ResponseInfo saveUser(@RequestBody @Validated(One.class) CoreUserDto coreUser, BindingResult result)
			throws Exception {
		super.validateBindingResult(result);
		coreUser.setIsAdmin(true);
		GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
		// 查询人员是否开启密级，不开启无视前台传值
		if (!attr.getOpenContentSecurity()) {
			coreUser.setUserSecretId(null);
		}
		return coreUserService.saveUser(coreUser);
	}

	/**
	 * 更新用户
	 * 
	 * @Title: update
	 * @param: @param
	 *             models
	 * @param: @param
	 *             result
	 * @param: @return
	 * @return: ResponseInfo
	 */
	@PutMapping()
	public ResponseInfo updateUser(@RequestBody @Validated(Two.class) CoreUserDto coreUser, BindingResult result)
			throws Exception {
		super.validateBindingResult(result);
		GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
		// 查询人员是否开启密级，不开启无视前台传值
		if (!attr.getOpenContentSecurity()) {
			coreUser.setUserSecretId(null);
		}
		return coreUserService.updateUser(coreUser);
	}

	/**
	 * 获取单个用户
	 * 
	 * @Title: get
	 * @Description:
	 * @param: id
	 * @return: ResponseInfo
	 */
	@Override
	@GetMapping("/{id}")
	@MoreSerializeField({
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "orgId", "email", "enabled",
					"telephone", "createTime", "createUser", "org", "userExt", "roleNames", "orgName", "userSecretId",
					"roleIds", "managerAble", "notCurrUser", "deleteAble" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "nodeIds" }),
			@SerializeField(clazz = CoreUserExt.class, includes = { "realname", "linephone" }) })
	public ResponseInfo get(@NotNull @PathVariable(value = "id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 删除管理员,根据可操作对象，判断是否可以操作 如果存在用户权限，删除用户则删除用户权限
	 * 
	 * @Title: delete
	 * @Description:
	 * @param: @param
	 *             ids
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping()
	public ResponseInfo delete(HttpServletRequest request, @RequestBody @Valid BeatchDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		CoreUser user = SystemContextUtils.getUser(request);
		return coreUserService.deleteUser(dto, user.getOrgId());
	}

	/**
	 * 后台管理用户重置密码
	 * 
	 * @Title: psw
	 * @param coreUser 传输DTO
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/psw")
	public ResponseInfo psw(HttpServletRequest request, @RequestBody CoreUserDto coreUser) throws GlobalException {
		CoreUser currUser = SystemContextUtils.getCoreUser();
		// 当前登录用户不能重置自己的密码
		if (currUser.getId().equals(coreUser.getId())) {
			return new ResponseInfo(UserErrorCodeEnum.PASSWORD_ERROR_RESET_MYSELF.getCode(),
					UserErrorCodeEnum.PASSWORD_ERROR_RESET_MYSELF.getDefaultMessage());
		}
		return coreUserService.psw(coreUser, true);
	}

	/**
	 * 自己修改密码
	 * 
	 * @Title: adminpsw
	 * @param coreUser
	 *            传输DTO
	 * @throws GlobalException
	 *             异常
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/adminpsw")
	public ResponseInfo adminpsw(HttpServletRequest request,
			@RequestBody @Validated(value = Three.class) CoreUserDto coreUser) throws GlobalException {
		CoreUser user = SystemContextUtils.getCoreUser();
		coreUser.setId(user.getId());
		// 原密码不能为空
		if (StringUtils.isBlank(coreUser.getOldpsw())) {
			// 密码为空
			return new ResponseInfo(UserErrorCodeEnum.PASSWORD_FORMAT_IS_INCORRECT.getCode(),
					UserErrorCodeEnum.PASSWORD_FORMAT_IS_INCORRECT.getDefaultMessage(), false);
		}
		// 解密旧密码
		String oldpStr = DesUtil.decrypt(coreUser.getOldpsw(), ContentSecurityConstants.DES_KEY,
				ContentSecurityConstants.DES_IV);
		// 判断输入的旧密码是否匹配
		if (!passwdService.isPasswordValid(oldpStr, user.getSalt(), user.getPassword())) {
			return new ResponseInfo(UserErrorCodeEnum.ACCOUNT_CREDENTIAL_ERROR.getCode(),
					UserErrorCodeEnum.ACCOUNT_CREDENTIAL_ERROR.getDefaultMessage(), false);
		}
		return coreUserService.psw(coreUser, false);
	}

	/**
	 * 启用用户
	 * 
	 * @Title: enableCoreUser
	 * @param dto
	 *            批量操作Dto
	 * @throws GlobalException
	 *             异常
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/on")
	public ResponseInfo enableCoreUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid BeatchDto dto, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return coreUserService.enableUser(dto);
	}

	/**
	 * 禁用用户
	 * 
	 * @Title: disableCoreUser
	 * @param dto
	 *            批量操作Dto
	 * @throws GlobalException
	 *             异常
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/off")
	public ResponseInfo disableCoreUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid BeatchDto dto, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return coreUserService.disableUser(dto);
	}

	/**
	 * 验证用户名
	 * 
	 * @param: validName
	 * @return
	 */
	@GetMapping(value = "/username/unique")
	public ResponseInfo username(@NotNull String username) throws GlobalException {
		Boolean flag = coreUserService.validName(username);
		return new ResponseInfo(flag);
	}

	/**
	 * 验证邮箱
	 * 
	 * @param: validMail
	 * @return
	 */
	@GetMapping(value = "/mail/unique")
	public ResponseInfo mail(String mail, Integer id) throws GlobalException {
		Boolean flag = coreUserService.validMail(mail, id);
		return new ResponseInfo(flag);
	}

	/**
	 * 验证手机
	 * 
	 * @param: validMail
	 * @return
	 */
	@GetMapping(value = "/phone/unique")
	public ResponseInfo validMail(String phone, Integer id) throws GlobalException {
		Boolean flag = coreUserService.validPhone(phone, id);
		return new ResponseInfo(flag);
	}

	/**
	 * 验证密码
	 * 
	 * @Title: validPsd
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/pwd/unique")
	public ThirdPartyResultDTO validPsd(@RequestBody PswDto dto) throws GlobalException {
		String psw = dto.getPsw();
		String username = dto.getUsername();
		if (StringUtils.isNotBlank(psw)) {
			return coreUserService.validPwd(psw, username);
		}
		return new ThirdPartyResultDTO(true);
	}

	/**
	 * 第三方管理员分页列表
	 */
	@GetMapping("/third/manager/page")
	@MoreSerializeField({
			@SerializeField(clazz = CoreUser.class, includes = { "id", "userExt", "username", "org", "roleNames" }),
			@SerializeField(clazz = CoreUserExt.class, includes = { "realname" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "name" }), })
	public ResponseInfo thirdManagerPage(@RequestParam(value = "orgId", required = false) Integer orgId,
			@RequestParam(value = "roleid", required = false) Integer roleid,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "appId") String appId, @RequestParam(value = "isWechat") boolean isWechat,
			Pageable pageable) throws GlobalException {
		Page<CoreUser> coreUserPage = null;
		// 如果传入的是微信公众号列表
		if (isWechat) {
			AbstractWeChatInfo info = weChatInfoService.findAppId(appId);
			List<CoreUser> coreUsers = info.getUsers();
			List<Integer> ids = null;
			if (coreUsers != null && coreUsers.size() > 0) {
				ids = coreUsers.stream().map(CoreUser::getId).collect(Collectors.toList());
			}
			coreUserPage = coreUserService.pageThirdManager(orgId, roleid, username, pageable, ids);
		}
		return new ResponseInfo(coreUserPage);
	}

}

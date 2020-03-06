/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.domain.dto.UserManagerDto;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.member.domain.MemberAttr;
import com.jeecms.member.domain.MemberGroup;
import com.jeecms.member.domain.MemberLevel;
import com.jeecms.member.service.MemberGroupService;
import com.jeecms.system.domain.CmsSite;

/**
 * 会员组管理controller层
 * 
 * @author: wulongwei
 * @date: 2019年4月15日 上午10:14:17
 */
@RestController
@RequestMapping("/memberGroup")
public class MemberGroupController extends BaseController<MemberGroup, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = { "groupName_LIKE" };
		super.setQueryParams(queryParams);
	}

	/**
	 * 分页查询
	 * 
	 * @Title: page
	 * @Description: 完成
	 * @param request 请求
	 * @param pageable 分页
	 * @throws GlobalException 异常
	 */
	@GetMapping("/page")
	@SerializeField(clazz = MemberGroup.class, includes = { "id", "groupName", "remark", "isDefault",
			"remarkInfo" })
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = "isDefault", direction = Direction.DESC) Pageable pageable) 
					throws GlobalException {
		return new ResponseInfo(service.getPage(super.getCommonParams(request), pageable, false));
	}
	
	/**
	 * 会员组列表
	 * @Title: list
	 * @Description: 完成
	 * @param request 请求
	 * @throws GlobalException 异常
	 */
	@GetMapping("/list")
	@SerializeField(clazz = MemberGroup.class, includes = { "id", "groupName", "isDefault" })
	public ResponseInfo list(HttpServletRequest request) 
					throws GlobalException {
		return new ResponseInfo(service.getList(super.getCommonParams(request), null, false));
	}

	/**
	 * 获取会员组详情
	 */
	@Override
	@GetMapping(value = "/{id}")
	@MoreSerializeField({
			@SerializeField(clazz = MemberGroup.class, includes = { "id", "groupName", "remark", 
					"isDefault", "views", "contributes", 
					"isAllChannelView", "isAllChannelContribute" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }) })
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 添加会员组信息
	 * 
	 * @Title: save
	 * @param memberGroup 信息
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PostMapping
	public ResponseInfo save(@RequestBody @Valid MemberGroup memberGroup) throws GlobalException {
		return groupService.saveMemberGroupInfo(memberGroup);
	}

	/**
	 * 修改会员组信息
	 * 
	 * @Title: update
	 * @param memberGroup 信息
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PutMapping
	public ResponseInfo update(@RequestBody @Valid MemberGroup memberGroup)
			throws GlobalException {
		return groupService.updateMemberGroupInfo(memberGroup);
	}

	/**
	 * 通过ID删除对象
	 */
	@DeleteMapping
	public ResponseInfo deleteIds(@RequestBody @Valid DeleteDto details) throws GlobalException {
		List<CoreUser> list = new ArrayList<CoreUser>(10);
		Integer[] ids = details.getIds();
		List<MemberGroup> groups = service.findAllById(Arrays.asList(ids));
		//同时删除会员所属等级的数据
		for (MemberGroup memberGroup : groups) {
			List<CoreUser> users = memberGroup.getUsers();
			if (!users.isEmpty()) {
				for (CoreUser user : users) {
					user.setGroupId(null);;
					user.setUserGroup(null);
					list.add(user);
				}
			}
		}
		service.delete(groups);
		coreUserService.batchUpdateAll(list);
		return new ResponseInfo();
	}
	
	/**
	 * 成员管理
	 * 
	 * @Title: userList
	 * @param groupId 会员组ID
	 *            会员组ID
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/list/users")
	@MoreSerializeField({
		@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "checkStatus", 
				"email", "admin", "sourceSite", "json", "enabled",
				"checkStatus", "integral", "userGroup", "userLevel", "userExt", "createTime" }),
		@SerializeField(clazz = CmsSite.class, includes = { "id", "name"}),
		@SerializeField(clazz = CoreUserExt.class, includes = { "realname", "gender", "remark",
				"birthday", "lockedTime", "lockedIp", "telephone" }),
		@SerializeField(clazz = MemberGroup.class, includes = { "id", "groupName", "remark", "isDefault" }),
		@SerializeField(clazz = MemberLevel.class, includes = { "id", "levelName", "remark", "integralMin",
				"integralMax" }), 
		@SerializeField(clazz = MemberAttr.class, includes = { "id", "attrName", "attrValue" }) 
		})
	public ResponseInfo userList(@NotNull Integer groupId, Pageable pageable, HttpServletRequest request)
			throws GlobalException {
		return new ResponseInfo(coreUserService.pageUser(null, null, null, null, false, 
				CoreUser.AUDIT_USER_STATUS_PASS,
				groupId, null, null, null,pageable));
	}

	/**
	 * 移动会员
	 * 
	 * @Title: userList
	 * @param: id
	 *             角色ID
	 * @return: ResponseInfo
	 */
	@PutMapping(value = "/list/users")
	public ResponseInfo remove(@RequestBody UserManagerDto dto) throws GlobalException {
		dto.setType(UserManagerDto.GROUP_TYPE);
		return coreUserService.moveUser(dto);
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
		dto.setType(UserManagerDto.GROUP_TYPE);
		return coreUserService.addUser(dto);
	}


	@Autowired
	private MemberGroupService groupService;
	@Autowired
	private CoreUserService coreUserService;
}

/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.member.domain.MemberLevel;
import com.jeecms.member.service.MemberLevelService;
import com.jeecms.resource.domain.ResourcesSpaceData;

/**
 * 会员等级controller
 * 
 * @author: wulongwei
 * @date: 2019年4月15日 上午11:11:30
 */
@RestController
@RequestMapping("/memberLevel")
public class MemberLevelController extends BaseController<MemberLevel, Integer> {

	@Autowired
	private CoreUserService coreUserService;
	
	@PostConstruct
	public void init() {
		String[] queryParams = { "levelName_LIKE" };
		super.setQueryParams(queryParams);
	}

	/**
	 * 分页查询
	 * 
	 * @param request 请求
	 * @param response 响应
	 * @param pageable 分页
	 * @return
	 */
	@GetMapping("/page")
	@MoreSerializeField({
			@SerializeField(clazz = MemberLevel.class, includes = { "id", "levelName", "integralMin", 
					"integralMax", "levelIcon", "remark", "logoResource", "remarkInfo" }),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "url" }) })
	public ResponseInfo page(HttpServletRequest request, HttpServletResponse response,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) 
					throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		String levelName = request.getParameter("levelName");
		if (StringUtils.isNotEmpty(levelName)) {
			params.put("LIKE_levelName_String", new String[] { levelName });
		}
		return new ResponseInfo(service.getPage(params, pageable, false));
	}
	
	/**
	 * 会员等级列表
	 * @Title: list
	 * @Description: 完成
	 * @param request 请求
	 * @throws GlobalException 异常
	 */
	@GetMapping("/list")
	@SerializeField(clazz = MemberLevel.class, includes = { "id", "levelName" })
	public ResponseInfo list(HttpServletRequest request) 
					throws GlobalException {
		return new ResponseInfo(service.getList(super.getCommonParams(request), null, false));
	}

	/**
	 * 获取会员等级详情
	 */
	@Override
	@GetMapping(value = "/{id}")
	@MoreSerializeField({
			@SerializeField(clazz = MemberLevel.class, includes = { "id", "levelName", "integralMin", 
					"integralMax", "levelIcon", "remark", "logoResource" }),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "url" }) })
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 修改会员等级信息
	 * 
	 * @Title: update
	 * @param memberLevel 等级
	 * @param response 响应
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PutMapping
	@MoreSerializeField({
		@SerializeField(clazz = MemberLevel.class, includes = { "id", "levelName" }),})
	public ResponseInfo update(@RequestBody @Valid MemberLevel memberLevel, HttpServletResponse response)
			throws GlobalException {
		return new ResponseInfo(levelService.updateMemberLevel(memberLevel));
	}

	/**
	 * 添加会员等级信息
	 * 
	 * @Title: save
	 * @param memberLevel 等级
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PostMapping
	@MoreSerializeField({
		@SerializeField(clazz = MemberLevel.class, includes = { "id", "levelName" }),})
	public ResponseInfo save(@RequestBody @Valid MemberLevel memberLevel) throws GlobalException {
		return new ResponseInfo(levelService.saveMemberLevel(memberLevel));
	}

	/**
	 * 通过ID删除对象
	 */
	@DeleteMapping
	public ResponseInfo deleteIds(@RequestBody @Valid DeleteDto details) throws GlobalException {
		List<CoreUser> list = new ArrayList<CoreUser>(10);
		Integer[] ids = details.getIds();
		List<MemberLevel> levels = service.findAllById(Arrays.asList(ids));
		//同时删除会员所属等级的数据
		for (MemberLevel memberLevel : levels) {
			List<CoreUser> users = memberLevel.getUsers();
			if (!users.isEmpty()) {
				for (CoreUser user : users) {
					user.setUserLevel(null);
					user.setLevelId(null);
					list.add(user);
				}
			}
		}
		coreUserService.batchUpdateAll(list);
		service.delete(levels);
		return new ResponseInfo();
	}

	@Autowired
	private MemberLevelService levelService;
}

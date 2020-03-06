/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.member.domain.MemberGroup;
import com.jeecms.member.domain.MemberLevel;
import com.jeecms.member.domain.MemberScoreDetails;
import com.jeecms.member.domain.dto.MemberDto;
import com.jeecms.member.domain.dto.MemberDto.One;
import com.jeecms.member.domain.dto.MemberDto.Two;
import com.jeecms.member.domain.vo.MemberInfoVo;
import com.jeecms.member.service.MemberScoreDetailsService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.dto.BeatchDto;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;

/**
 * 会员管理控制器
 * 
 * @author: ljw
 * @date: 2019年4月17日 上午11:33:21
 */
@RequestMapping("/members")
@RestController
public class MemberController extends BaseAdminController<CoreUser, Integer> {

	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private GlobalConfigService globalConfigService;
	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private MemberScoreDetailsService memberScoreDetailsService;
	  
	/**
	 * 待审核会员(包含审核不通过)
	 * 
	 * @Title: audit
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/audit")
	public ResponseInfo audit(HttpServletRequest request, @RequestParam("checkStatus") Short checkStatus,
			@RequestParam("key") String key, @RequestParam("sourceSite") Integer sourceSiteId,
			Pageable pageable)
			throws GlobalException {
		List<Integer> siteIds = new ArrayList<Integer>(10);
		// 查询可管理站点ID
		List<Integer> ids = SystemContextUtils.getUser(request).getOwnerSiteIds();
		if (sourceSiteId != null) {
			// 判断可管理站点是否包含传递来的站点
			if (ids.contains(sourceSiteId)) {
				siteIds.add(sourceSiteId);
			} else {
				return new ResponseInfo();
			}
		} else {
			siteIds.addAll(ids);
		}
		// 判断会员审核是否开启
		Boolean flag = globalConfigService.get().getConfigAttr().getMemberRegisterExamine();
		if (flag == null || !flag) {
			//如果不开启，把待审核的会员改成审核通过；这里做接口限制
			List<CoreUser> users = coreUserService.findList(null, null, null, key, false,
					CoreUser.AUDIT_USER_STATUS_WAIT, null, null, null, siteIds, null);
			for (CoreUser coreUser : users) {
				coreUser.setCheckStatus(CoreUser.AUDIT_USER_STATUS_PASS);
				//注册积分信息
				memberScoreDetailsService.addMemberScore(MemberScoreDetails.REGISTER_SCORE_TYPE, 
						coreUser.getId(), coreUser.getSourceSiteId(), null);
				// 注册积分信息
				memberScoreDetailsService.addMemberScore(MemberScoreDetails.MESSAGE_SCORE_TYPE, 
						coreUser.getId(),
						coreUser.getSourceSiteId(), null);
			}
			coreUserService.batchUpdate(users);
			return new ResponseInfo();
		}
		
		List<JSONObject> list = new ArrayList<JSONObject>(); 
		Page<CoreUser> page = coreUserService.pageUser(null, null, null, key, 
				false, checkStatus, null, null,
				null, siteIds, pageable);
		List<CoreUser> user = page.getContent();
		for (CoreUser coreUser : user) {
			JSONObject obj = coreUserService.initDefaultModelItems(coreUser, true);
			list.add(obj);
		}
		Page<JSONObject> pages = new PageImpl<JSONObject>(list, pageable, page.getTotalElements());
		return new ResponseInfo(pages);
	}

	/**
	 * 审核通过
	 * 
	 * @Title: on
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/on")
	public ResponseInfo on(@RequestBody @Valid DeleteDto dto, BindingResult result) throws GlobalException {
		return coreUserService.auditON(dto.getIds());
	}

	/**
	 * 审核不通过
	 * 
	 * @Title: off
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/off")
	public ResponseInfo off(@RequestBody @Valid BeatchDto dto, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return coreUserService.auditOFF(dto);
	}

	/**
	 * 会员管理列表分页
	 * 
	 * @Title: page
	 * @return: ResponseInfo
	 */
	@GetMapping()
	@MoreSerializeField({
			@SerializeField(clazz = CoreUser.class, includes = {
					"id", "username", "email", "sourceSite", 
					"enabled", "integral", "userGroup", "userLevel", "userExt" }),
			@SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CoreUserExt.class, includes = { "realname" }),
			@SerializeField(clazz = MemberGroup.class, includes = { "groupName" }),
			@SerializeField(clazz = MemberLevel.class, includes = { "levelName" }) })
	public ResponseInfo page(HttpServletRequest request, @RequestParam("enabled") Boolean enabled,
			@RequestParam("key") String key, @RequestParam("groupId") Integer groupId,
			@RequestParam("levelId") Integer levelId, @RequestParam("sourceSite") Integer sourceSiteId,
			Pageable pageable)
			throws GlobalException {
		List<Integer> siteIds = new ArrayList<Integer>(10);
		// 查询可管理站点ID
		List<Integer> ids = SystemContextUtils.getUser(request).getOwnerSiteIds();
		if (sourceSiteId != null) {
			// 判断可管理站点是否包含传递来的站点
			if (ids.contains(sourceSiteId)) {
				siteIds.add(sourceSiteId);
			} else {
				return new ResponseInfo();
			}
		} else {
			siteIds.addAll(ids);
		}
		// 判断会员审核是否开启
		Boolean flag = globalConfigService.get().getConfigAttr().getMemberRegisterExamine();
		if (flag == null || !flag) {
			//如果不开启，把待审核的会员改成审核通过;
			List<CoreUser> users = coreUserService.findList(null, null, null, key, false,
					CoreUser.AUDIT_USER_STATUS_WAIT, null, null, null, siteIds, null);
			for (CoreUser coreUser : users) {
				coreUser.setCheckStatus(CoreUser.AUDIT_USER_STATUS_PASS);
			}
			coreUserService.batchUpdate(users);
			coreUserService.flush();
		}
		return new ResponseInfo(coreUserService.pageUser(enabled, null, null, key, false,
				CoreUser.AUDIT_USER_STATUS_PASS, groupId, levelId, null, siteIds, pageable));
	}

	/**
	 * 后台会员添加
	 * 
	 * @Title: saveUser
	 * @return: ResponseInfo
	 */
	@PostMapping
	public ResponseInfo saveMember(HttpServletRequest request, @RequestBody 
			@Validated(One.class) MemberDto coreUser,
			BindingResult result) throws Exception {
		super.validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		coreUser.setSiteId(siteId);
		return coreUserService.saveMember(coreUser);
	}

	/**
	 * 会员修改
	 * 
	 * @Title: saveUser
	 * @return: ResponseInfo
	 */
	@PutMapping
	public ResponseInfo updateMember(@RequestBody @Validated(Two.class) MemberDto coreUser, BindingResult result)
			throws Exception {
		super.validateBindingResult(result);
		return coreUserService.updateMember(coreUser);
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
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "url" }),
			})
	public ResponseInfo get(@NotNull @PathVariable(value = "id") Integer id) throws GlobalException {
		MemberInfoVo vo = coreUserService.getMemberInfo(id);
		return new ResponseInfo(vo);
	}

	/**
	 * 积分配置详情
	 * 
	 * @Title: get
	 * @Description:
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/score")
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "cmsSiteCfg" }),
			@SerializeField(clazz = CmsSiteConfig.class, includes = { "submitStatus", "submitSuccessNumber",
					"submitSuccessScore", "submitScoreLimit", "submitOnedayMaxScore", 
					"commentStatus",
					"commentSuccessNumber", "commentSuccessScore", "commentScoreLimit", 
					"commentOnedayMaxScore",
					"registerStatus", "registerSuccessScore", "perfectMessageStatus",
					"perfectMessageSuccessScore" }), })
	public ResponseInfo score(HttpServletRequest request) throws GlobalException {
		// 获取当前站点的ID
		Integer siteId = SystemContextUtils.getSiteId(request);
		CmsSite site = cmsSiteService.findById(siteId);
		return new ResponseInfo(site);
	}

	/**
	 * 修改积分配置
	 * 
	 * @Title: updateScore
	 * @Description:
	 * @param: map
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/score")
	public ResponseInfo updateScore(HttpServletRequest request, @RequestBody Map<String, String> map)
			throws GlobalException {
		// 获取当前站点的ID
		Integer siteId = SystemContextUtils.getSiteId(request);
		return coreUserService.updateScore(siteId, map);
	}

	/**
	 * 删除会员,根据当前登录人可管理的站点判断
	 * 
	 * @Title: delete
	 * @param request 请求
	 * @param dto     传输
	 * @param result  结果
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@DeleteMapping()
	public ResponseInfo delete(HttpServletRequest request, @RequestBody @Valid BeatchDto dto, BindingResult result)
			throws GlobalException {
		List<CoreUser> users = coreUserService.findAllById(dto.getIds());
		// 查询可管理站点ID
		List<Integer> siteIds = SystemContextUtils.getUser(request).getOwnerSiteIds();
		for (CoreUser user : users) {
			if (!siteIds.contains(user.getSourceSiteId())) {
				return new ResponseInfo(SysOtherErrorCodeEnum.NO_USER_PERMISSION_ERROR.getCode(),
						SysOtherErrorCodeEnum.NO_USER_PERMISSION_ERROR.getDefaultMessage());
			}
		}
		coreUserService.delete(users);
		return new ResponseInfo();
	}

	/**
	 * 启用用户
	 * @Title: enableCoreUser
	 * @param dto 批量操作Dto
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/enable")
	public ResponseInfo enableCoreUser(HttpServletRequest request, @RequestBody @Valid BeatchDto dto,
			BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		List<CoreUser> users = coreUserService.findAllById(dto.getIds());
		// 查询可管理站点ID
		List<Integer> siteIds = SystemContextUtils.getUser(request).getOwnerSiteIds();
		for (CoreUser user : users) {
			if (!siteIds.contains(user.getSourceSiteId())) {
				return new ResponseInfo(SysOtherErrorCodeEnum.NO_USER_PERMISSION_ERROR.getCode(),
						SysOtherErrorCodeEnum.NO_USER_PERMISSION_ERROR.getDefaultMessage());
			}
		}
		for (CoreUser user : users) {
			user.setEnabled(true);
		}
		coreUserService.batchUpdate(users);
		return new ResponseInfo();
	}

	/**
	 * 禁用用户
	 * 
	 * @Title: disableCoreUser
	 * @param dto 批量操作Dto
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/disable")
	public ResponseInfo disableCoreUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid BeatchDto dto, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		List<CoreUser> users = coreUserService.findAllById(dto.getIds());
		// 查询可管理站点ID
		List<Integer> siteIds = SystemContextUtils.getUser(request).getOwnerSiteIds();
		for (CoreUser user : users) {
			if (!siteIds.contains(user.getSourceSiteId())) {
				return new ResponseInfo(SysOtherErrorCodeEnum.NO_USER_PERMISSION_ERROR.getCode(),
						SysOtherErrorCodeEnum.NO_USER_PERMISSION_ERROR.getDefaultMessage());
			}
		}
		for (CoreUser user : users) {
			user.setEnabled(false);
		}
		coreUserService.batchUpdate(users);
		return new ResponseInfo();
	}

	/**
	 * 当新增时查询会员字段
	 * 
	 * @Title: addParam
	 * @param request 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@MoreSerializeField({ @SerializeField(clazz = CmsModel.class, includes = { "enableJson" }) })
	@GetMapping(value = "/addition/model")
	public ResponseInfo addParam(HttpServletRequest request) throws GlobalException {
		return new ResponseInfo(cmsModelService.getInfo(null));
	}

	/**
	 * 待审核会员列表动态字段
	 * 
	 * @Title: addition
	 * @param request 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/addition")
	public ResponseInfo addition(HttpServletRequest request) throws GlobalException {
		List<JSONObject> list = new ArrayList<JSONObject>(10);
		//得到会员模型字段
		CmsModel model = cmsModelService.getFrontMemberModel();
		JSONObject obj = model.getEnableJson();
		JSONArray array = obj.getJSONArray("formListBase");
		if (array != null) {
			//应需求，需要把后台模型字段用户名排在首位
			for (Object object : array) {
				JSONObject json = (JSONObject) object;
				JSONObject jsonObj = new JSONObject();
				//得到用户名
				if (json.getString("type").equals(CmsModelConstant.FIELD_MEMBER_USERNAME)) {
					jsonObj.put("label", json.getJSONObject("value").getString("label"));
					jsonObj.put("value", json.getString("type"));
					list.add(jsonObj);
					break;
				}
			}
			for (Object object : array) {
				JSONObject json = (JSONObject) object;
				JSONObject jsonObj = new JSONObject();
				//过滤密码，过滤用户名
				if (json.getString("type").equals(CmsModelConstant.FIELD_MEMBER_PASSWORD)
					|| json.getString("type").equals(CmsModelConstant.FIELD_MEMBER_USERNAME)) {
					continue;
				}
				jsonObj.put("label", json.getJSONObject("value").getString("label"));
				jsonObj.put("value", json.getString("type"));
				list.add(jsonObj);
			}
		}
		JSONObject json1 = new JSONObject();
		json1.put("label", "注册站点");
		json1.put("value", "sourceSiteName");
		list.add(json1);
		JSONObject json2 = new JSONObject();
		json2.put("label", "注册时间");
		json2.put("value", "createTime");
		list.add(json2);
		JSONObject json3 = new JSONObject();
		json3.put("label", "状态");
		json3.put("value", "checkStatus");
		list.add(json3);
		return new ResponseInfo(list);
	}

}

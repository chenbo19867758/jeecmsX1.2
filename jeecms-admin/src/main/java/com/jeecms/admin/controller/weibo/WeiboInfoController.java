/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.weibo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.Constants;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.domain.dto.SetAdminDto;
import com.jeecms.weibo.service.WeiboInfoService;

/**
 * 微博账户控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2019-06-17
 */
@RequestMapping("/weiboinfo")
@RestController
public class WeiboInfoController extends BaseController<WeiboInfo, Integer> {

	@Autowired
	private WeiboInfoService weiboInfoService;
	@Autowired
	private CoreUserService coreUserService;
	
	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 列表分页
	 * @Title: page
	 * @param: request 请求
	 * @param: pageable 分页
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping()
	@MoreSerializeField({
		@SerializeField(clazz = WeiboInfo.class, includes = { "id", "screenName", "profileImageUrl", 
				"followersCount", "friendsCount", "statusesCount", "red", "uid",
				"adminNames", "residueTime" }),
		 })
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = "createTime", direction = Direction.ASC) Pageable pageable)
					throws GlobalException {
		//列表根据当前站点来筛选
		Integer siteId = SystemContextUtils.getSiteId(request);
		Map<String, String[]> params = super.getCommonParams(request);
		// 排除非站点数据
		params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
		return new ResponseInfo(weiboInfoService.getPage(params, pageable, false));
	}
	
	/**
	 * 账户列表
	 * @Title: page
	 * @param: request 请求
	 * @param: pageable 分页
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping("/list")
	@MoreSerializeField({
		@SerializeField(clazz = WeiboInfo.class, includes = { "id", "screenName", "profileImageUrl", "uid"}),
		 })
	public ResponseInfo list(HttpServletRequest request, Paginable paginable)
					throws GlobalException {
		//得到用户可管理的微博账户
		CoreUser user = SystemContextUtils.getUser(request);
		List<WeiboInfo> infos = user.getWeiboInfos();
		if (!infos.isEmpty()) {
			infos = infos.stream().sorted(Comparator.comparing(WeiboInfo::getCreateTime))
			.collect(Collectors.toList());
		}
		return new ResponseInfo(infos);
	}
	
	/**
	 * 微博账户详情
	 * @Title: page
	 * @param: request 请求
	 * @param: pageable 分页
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping("/{uid}")
	@MoreSerializeField({
		@SerializeField(clazz = WeiboInfo.class, includes = { "id", "screenName", "profileImageUrl", 
				"followersCount", "friendsCount", "statusesCount", "gender", "location", "uid" }),
		 })
	public ResponseInfo getInfo(@NotNull @PathVariable(value = "uid") String uid)
					throws GlobalException {
		return new ResponseInfo(weiboInfoService.findWeiboInfo(uid));
	}

	/**
	 * 取消授权
	 * 
	 * @Title: revoke
	 * @param dels 传输Dto
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@DeleteMapping()
	public ResponseInfo revoke(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		List<WeiboInfo> infos = weiboInfoService.findAllById(Arrays.asList(dels.getIds()));
		for (WeiboInfo info : infos) {
			Map<String, String> params = new HashMap<String, String>(16);
			params.put("access_token", info.getAccessToken());
			HttpUtil.get(Constants.REVOKE_AUTHORIZE_URL, params);
			// 逻辑删除该微博账户
			weiboInfoService.delete(info);
		}
		return new ResponseInfo();
	}
	
	/**
	 * 设置管理员
	 * @Title: revoke
	 * @param dto 传输Dto
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping("/user")
	public ResponseInfo revoke(@RequestBody @Valid SetAdminDto dto) throws GlobalException {
		WeiboInfo info = weiboInfoService.findById(dto.getWeiboUserId());
		List<CoreUser> users = coreUserService.findAllById(dto.getIds());
		info.setCoreUsers(users);
		weiboInfoService.update(info);
		return new ResponseInfo();
	}
	
	/**
	 * 微博管理员列表
	* @Title: list 
	* @param weiboUserId 微博用户ID
	* @throws GlobalException 异常
	 */
	@GetMapping("/admins")
	@MoreSerializeField({
		@SerializeField(clazz = CoreUser.class, includes = { "id","userExt",
				"username","org","roleNames"}),
		@SerializeField(clazz = CoreUserExt.class, includes = { "realname"}),
		@SerializeField(clazz = CmsOrg.class, includes = { "name"}),
	})
	public ResponseInfo admins(Integer weiboUserId) throws GlobalException {
		WeiboInfo info = weiboInfoService.findById(weiboUserId);
		List<CoreUser> users = info.getCoreUsers();
		return new ResponseInfo(users);
	}
}

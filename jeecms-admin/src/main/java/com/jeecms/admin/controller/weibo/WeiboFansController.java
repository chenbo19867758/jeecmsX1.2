/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.weibo;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.weibo.domain.WeiboFans;
import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.service.WeiboFansService;
import com.jeecms.weibo.service.WeiboInfoService;

/**
 * 微博粉丝控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2019-06-18
 */
@RequestMapping("/weibofans")
@RestController
public class WeiboFansController extends BaseController<WeiboFans, Integer> {

	@Autowired
	private WeiboFansService weiboFansService;
	@Autowired
	private WeiboInfoService weiboInfoService;
	
	/**
	 * 初始化
	* @Title: init 
	 */
	@PostConstruct
	public void init() {
		String[] queryParams = {
					// 性别
					"gender_EQ",
					// 昵称
					"screenName_LIKE", };
		super.setQueryParams(queryParams);
	}

	/**
	 * 微博粉丝列表分页
	* @Title: page 
	* @param request 请求
	* @param pageable 分页对象
	* @return ResponseInfo
	* @throws GlobalException 异常
	 */
	@GetMapping()
	@MoreSerializeField({ @SerializeField(clazz = WeiboFans.class, includes = {
			"id", "screenName", "profileImageUrl", "description",
			"followersCount", "friendsCount", "statusesCount", "gender", "location", "blogUrl" }), })
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = "id", direction = Direction.ASC) Pageable pageable) 
					throws GlobalException {
		String id = request.getParameter("id");
		WeiboInfo info = weiboInfoService.findById(Integer.valueOf(id));
		Map<String, String[]> params = super.getCommonParams(request);
		params.put("EQ_uid_Long", new String[] { info.getUid() });
		Page<WeiboFans> page = weiboFansService.getPage(params, pageable, false);
		return new ResponseInfo(page);
	}


	/**
	 * 同步粉丝
	 * @Title: sync   
	 * @param: request 请求
	 * @param: uid 账户UID
	 * @return: ResponseInfo
	 */
	@GetMapping("/sync")
	public ResponseInfo sync(HttpServletRequest request, @NotNull Integer id) throws GlobalException {	
		Integer userId = SystemContextUtils.getUserId(request);
		weiboInfoService.checkWeiboAuth(userId, id);
		return weiboFansService.sync(id);
	}
}

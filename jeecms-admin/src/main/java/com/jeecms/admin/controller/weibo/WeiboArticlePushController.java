/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.weibo;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.weibo.domain.WeiboArticlePush;
import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.domain.dto.PreviewDto;
import com.jeecms.weibo.domain.dto.PushDto;
import com.jeecms.weibo.service.WeiboArticlePushService;
import com.jeecms.weibo.service.WeiboInfoService;

/**
 * 文章推送控制器
 * @author: ljw
 * @version: 1.0
 * @date 2019-06-18
 * 
 */
@RequestMapping("/weiboarticlepush")
@RestController
public class WeiboArticlePushController extends BaseController<WeiboArticlePush, Integer> {

	@Autowired
	private WeiboArticlePushService weiboArticlePushService;
	@Autowired
	private WeiboInfoService weiboInfoService;
	
	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**推送记录
	 * @Title: 列表分页
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping("/page")
	@MoreSerializeField({ 
		@SerializeField(clazz = WeiboArticlePush.class, includes = {
			"id", "contentId", "uid", "title","createTime","createUser",
			"articleSourceUrl", "articleWeiboUrl", "pushResult", "weiboInfo" }),
		@SerializeField(clazz = WeiboInfo.class, includes = {
				"screenName" }),
		})
	public ResponseInfo page(HttpServletRequest request,
			@RequestParam("uid") Long uid, @RequestParam("title") String title,
			@RequestParam("status") Integer status,
			@PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable) 
					throws GlobalException {
		Page<WeiboArticlePush> page = weiboArticlePushService.page(uid, title,status, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 推送内容到新浪微博
	 * @Title: push
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping()
	public ResponseInfo push(HttpServletRequest request,@RequestBody PushDto dto, BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		//得到站点ID
		Integer siteId = SystemContextUtils.getSiteId(request);
		dto.setSiteId(siteId);
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		for (Integer weiboId : dto.getWeiboIds()) {
			weiboInfoService.checkWeiboAuth(userId, weiboId);
		}
		return weiboArticlePushService.push(dto);
	}
	
	/**
	 * 推送预览
	 * @Title: push
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping("/preview")
	public ResponseInfo preview(HttpServletRequest request,@RequestBody PreviewDto dto, BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		return weiboArticlePushService.preview(dto);
	}
	
	/**
	 * 编辑后推送到新浪微博
	 * @Title: push
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping("/updatePush")
	public ResponseInfo updatePush(HttpServletRequest request,@RequestBody PreviewDto dto, BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		//得到站点ID
		Integer siteId = SystemContextUtils.getSiteId(request);
		dto.setSiteId(siteId);
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		weiboInfoService.checkWeiboAuth(userId, dto.getWeiboId());
		return weiboArticlePushService.updatePush(dto);
	}

}

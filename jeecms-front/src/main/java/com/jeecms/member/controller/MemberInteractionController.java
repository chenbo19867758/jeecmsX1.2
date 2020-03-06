/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  
package com.jeecms.member.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.content.domain.ContentAttrRes;
import com.jeecms.content.domain.vo.ContentFrontVo;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.domain.vo.UserInteractionMoblieVo;
import com.jeecms.interact.domain.vo.UserInteractionVo;
import com.jeecms.interact.service.UserCommentService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.util.SystemContextUtils;

/**   
 * 我的互动
 * @author: ljw
 * @date:   2019年7月20日 上午11:43:18     
 */
@RestController
@RequestMapping("/interaction")
public class MemberInteractionController extends BaseController<UserComment, Integer> {

	@Autowired
	private UserCommentService userCommentService;
	
	/**
	 * 我的互动列表
	* @Title: reportPage 
	* @param request 请求
	* @param startTime 开始时间
	* @param endTime 结束时间
	* @param pageable 分页
	* @throws GlobalException 异常
	 */
	@GetMapping("/page")
	public ResponseInfo reportPage(HttpServletRequest request,
			Date startTime, Date endTime, Pageable pageable) 
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		Page<UserInteractionVo> uComments = userCommentService.interactPage(startTime, endTime, userId, pageable);
		return new ResponseInfo(uComments);
	}
	
	/**
	 * 删除评论
	* @Title: deleteComment 
	* @param id 评论ID
	* @throws GlobalException 异常
	 */
	@GetMapping("/{id}")
	public ResponseInfo deleteComment(@NotNull @PathVariable(value = "id") Integer id) 
			throws GlobalException {
		userCommentService.delete(id);
		return new ResponseInfo();
	}
	
	/**
	 * 批量删除评论
	* @Title: deleteComment 
	* @param dto 删除DTO
	* @throws GlobalException 异常
	 */
	@DeleteMapping()
	public ResponseInfo deleteComment(@RequestBody DeleteDto dto) 
			throws GlobalException {
		userCommentService.delete(dto.getIds());
		return new ResponseInfo();
	}
	
	/**
	 * 手机端我的互动列表
	* @Title: reportPage 
	* @param request 请求
	* @param pageable 分页
	* @throws GlobalException 异常
	 */
	@GetMapping("/mobilePage")
	@MoreSerializeField({
		@SerializeField(clazz = ContentFrontVo.class, excludes = {"titleIsBold","titleColor", 
				}),
		@SerializeField(clazz = ContentAttr.class, includes = {"resourcesSpaceData","contentAttrRes"}),
		@SerializeField(clazz = ContentAttrRes.class, includes = {"resourcesSpaceData"}),
		@SerializeField(clazz = ResourcesSpaceData.class, includes = {"resourceType","dimensions","url",
				"resourceDate","suffix"})
	})
	public ResponseInfo mobilePage(HttpServletRequest request, Pageable pageable) 
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		Page<UserInteractionMoblieVo> uComments = userCommentService.mobilePage(userId, pageable);
		return new ResponseInfo(uComments);
	}
	
	/**
	 * 一键清空我的互动
	* @Title: clear 
	* @param request 请求
	* @throws GlobalException 异常
	 */
	@GetMapping("/clear")
	public ResponseInfo clear(HttpServletRequest request) 
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		return userCommentService.clear(userId);
	}
}
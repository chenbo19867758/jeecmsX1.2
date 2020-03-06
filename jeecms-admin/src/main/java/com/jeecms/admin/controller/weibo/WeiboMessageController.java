/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.weibo;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.weibo.api.comment.WeiboCommentService;
import com.jeecms.common.weibo.api.statuses.WeiboStatusesService;
import com.jeecms.common.weibo.bean.request.comment.CommentCreateRequest;
import com.jeecms.common.weibo.bean.request.comment.DeleteCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.ReplyCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.UserCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.WeiboCommentRequest;
import com.jeecms.common.weibo.bean.request.statuses.StatusesRequest;
import com.jeecms.common.weibo.bean.response.comment.vo.CommentVO;
import com.jeecms.common.weibo.bean.response.statuses.vo.StatusesVO;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.domain.dto.CreateCommentDto;
import com.jeecms.weibo.service.WeiboInfoService;

/**
 * 微博信息管理控制器
 * 
 * @author: ljw
 * @date: 2019年6月19日 下午2:19:18
 */
@RequestMapping("/weibomessage")
@RestController
public class WeiboMessageController {

	@Autowired
	private WeiboCommentService weiboCommentService;
	@Autowired
	private WeiboInfoService weiboInfoService;
	@Autowired
	private WeiboStatusesService weiboStatusesService;

	/**
	 * 获取@ 我的微博
	 * 
	 * @Title: statuses
	 * @param id 微博账户ID
	 * @param request 请求
	 * @param pageable 分页对象
	 * @return
	 */
	@GetMapping("/status")
	@MoreSerializeField({ 
		@SerializeField(clazz = WeiboUserResponse.class, includes = {
			"idstr", "location", "screenName", "profileImageUrl", "followersCount", "friendsCount",
			"statusesCount", "gender", }),
		})
	public ResponseInfo statuses(HttpServletRequest request, @NotNull Integer id, Pageable pageable) 
			throws GlobalException {
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(id);
		StatusesRequest sr = new StatusesRequest();
		sr.setAccessToken(info.getAccessToken());
		sr.setPage(pageable.getPageNumber() + 1);
		sr.setCount(pageable.getPageSize());
		List<StatusesVO> vos = weiboStatusesService.getInfo(sr);
		Integer sum = 0;
		if (!vos.isEmpty()) {
			sum = vos.get(0).getTotalNumber();
		}
		PageImpl<StatusesVO> page = new PageImpl<StatusesVO>(vos, pageable, sum);
		return new ResponseInfo(page);
	}
	
	/**
	 * 根据微博ID获取评论列表
	 * 
	 * @Title: statuses
	 * @param id 微博ID
	 * @param request 请求
	 * @param pageable 分页对象
	 * @return
	 */
	@GetMapping("/comments")
	@MoreSerializeField({ 
		@SerializeField(clazz = WeiboUserResponse.class, includes = {
			"idstr", "location", "screenName", "profileImageUrl", "followersCount", "friendsCount",
			"statusesCount", "gender", }),
		})
	public ResponseInfo comments(HttpServletRequest request, @NotNull Integer id,
			@NotNull Long weiboId, Pageable pageable) 
			throws GlobalException {
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(id);
		WeiboCommentRequest sr = new WeiboCommentRequest();
		sr.setAccessToken(info.getAccessToken());
		sr.setId(weiboId);
		sr.setPage(pageable.getPageNumber() + 1);
		sr.setCount(pageable.getPageSize());
		List<CommentVO> vos = weiboCommentService.findByWeiboID(sr);
		Integer sum = 0;
		if (!vos.isEmpty()) {
			sum = vos.get(0).getTotalNumber();
		}
		PageImpl<CommentVO> page = new PageImpl<CommentVO>(vos, pageable, sum);
		return new ResponseInfo(page);

	}
	
	/**
	 * 根据微博id 评论
	 * 
	 * @Title: statuses
	 * @param dto 传输Dto
	 * @param request 请求
	 * @return
	 */
	@PostMapping()
	public ResponseInfo comment(HttpServletRequest request, @RequestBody CreateCommentDto dto) 
			throws GlobalException {
		//检查权限
		Integer userId = SystemContextUtils.getUserId(request);
		weiboInfoService.checkWeiboAuth(userId, dto.getWeiboUserId());
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(dto.getWeiboUserId());
		CommentCreateRequest sr = new CommentCreateRequest();
		sr.setComment(dto.getComment());
		sr.setId(dto.getWeiboId());
		sr.setAccessToken(info.getAccessToken());
		CommentVO vo = weiboCommentService.createComment(sr);
		return new ResponseInfo(vo);
	}
	
	/**
	 * 根据评论id删除评论
	 * @Title: statuses
	 * @param dto 传输Dto
	 * @param request 请求
	 * @return
	 */
	@DeleteMapping()
	public ResponseInfo delComment(HttpServletRequest request, @RequestBody CreateCommentDto dto) 
			throws GlobalException {
		//检查权限
		Integer userId = SystemContextUtils.getUserId(request);
		weiboInfoService.checkWeiboAuth(userId, dto.getWeiboUserId());
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(dto.getWeiboUserId());
		DeleteCommentRequest sr = new DeleteCommentRequest();
		sr.setCid(dto.getCommentId());
		sr.setAccessToken(info.getAccessToken());
		weiboCommentService.deleteComment(sr);
		return new ResponseInfo();
	}
	
	/**
	 * 回复评论
	 * @Title: statuses
	 * @param dto 传输Dto
	 * @param request 请求
	 * @return
	 */
	@PutMapping()
	public ResponseInfo replyComment(HttpServletRequest request, @RequestBody CreateCommentDto dto) 
			throws GlobalException {
		//检查权限
		Integer userId = SystemContextUtils.getUserId(request);
		weiboInfoService.checkWeiboAuth(userId, dto.getWeiboUserId());
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(dto.getWeiboUserId());
		ReplyCommentRequest sr = new ReplyCommentRequest();
		sr.setComment(dto.getComment());
		sr.setId(dto.getWeiboId());
		sr.setCid(dto.getCommentId());
		sr.setAccessToken(info.getAccessToken());
		CommentVO vo = weiboCommentService.replyComment(sr);
		return new ResponseInfo(vo);
	}
	
	/**
	 * @ 我的评论
	 * @Title: statuses
	 * @param id 微博账户ID
	 * @param request 请求
	 * @return
	 */
	@GetMapping("/atcomments")
	@MoreSerializeField({ 
		@SerializeField(clazz = CommentVO.class, excludes = {
				"replys", "replyCommentId" }),
		@SerializeField(clazz = WeiboUserResponse.class, includes = {
			"idstr", "location", "screenName", "profileImageUrl","followersCount","friendsCount",
			"statusesCount", "gender", }),
		})
	public ResponseInfo atcomments(HttpServletRequest request, @NotNull Integer id,Pageable pageable) 
			throws GlobalException {
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(id);
		UserCommentRequest sr = new UserCommentRequest();
		sr.setAccessToken(info.getAccessToken());
		sr.setPage(pageable.getPageNumber() + 1);
		sr.setCount(pageable.getPageSize());
		List<CommentVO> vos = weiboCommentService.getInfo(sr);
		Integer sum = 0;
		if (!vos.isEmpty()) {
			sum = vos.get(0).getTotalNumber();
		}
		PageImpl<CommentVO> page = new PageImpl<CommentVO>(vos, pageable, sum);
		return new ResponseInfo(page);
	}
	
	/**
	 * 收到的评论
	 * @Title: statuses
	 * @param id 微博账户ID
	 * @param request 请求
	 * @return
	 */
	@GetMapping("/recComments")
	@MoreSerializeField({ 
		@SerializeField(clazz = CommentVO.class, excludes = {
				"replys", "replyCommentId" }),
		@SerializeField(clazz = WeiboUserResponse.class, includes = {
			"idstr", "location", "screenName", "profileImageUrl","followersCount","friendsCount",
			"statusesCount", "gender", }),
		})
	public ResponseInfo receiveComments(HttpServletRequest request, @NotNull Integer id,Pageable pageable) 
			throws GlobalException {
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(id);
		UserCommentRequest sr = new UserCommentRequest();
		sr.setAccessToken(info.getAccessToken());
		sr.setPage(pageable.getPageNumber() + 1);
		sr.setCount(pageable.getPageSize());
		List<CommentVO> vos = weiboCommentService.receiveInfo(sr);
		Integer sum = 0;
		if (!vos.isEmpty()) {
			sum = vos.get(0).getTotalNumber();
		}
		PageImpl<CommentVO> page = new PageImpl<CommentVO>(vos, pageable, sum);
		return new ResponseInfo(page);
	}
	
	/**
	 * 发出的评论
	 * @Title: statuses
	 * @param id 微博账户ID
	 * @param request 请求
	 * @return
	 */
	@GetMapping("/sendComments")
	@MoreSerializeField({ 
		@SerializeField(clazz = CommentVO.class, excludes = {
				"replys", "replyCommentId" }),
		@SerializeField(clazz = WeiboUserResponse.class, includes = {
			"idstr", "location", "screenName", "profileImageUrl","followersCount","friendsCount",
			"statusesCount", "gender", }),
		})
	public ResponseInfo sendComments(HttpServletRequest request, @NotNull Integer id,Pageable pageable) 
			throws GlobalException {
		// 查询账户信息
		WeiboInfo info = weiboInfoService.findById(id);
		UserCommentRequest sr = new UserCommentRequest();
		sr.setAccessToken(info.getAccessToken());
		sr.setPage(pageable.getPageNumber() + 1);
		sr.setCount(pageable.getPageSize());
		List<CommentVO> vos = weiboCommentService.sendInfo(sr);
		Integer sum = 0;
		if (!vos.isEmpty()) {
			sum = vos.get(0).getTotalNumber();
		}
		PageImpl<CommentVO> page = new PageImpl<CommentVO>(vos, pageable, sum);
		return new ResponseInfo(page);
	}
	
}

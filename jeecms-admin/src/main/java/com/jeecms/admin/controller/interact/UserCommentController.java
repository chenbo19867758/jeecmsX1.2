/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.interact;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.Content;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.domain.UserCommentReport;
import com.jeecms.interact.domain.dto.UserCommentDto;
import com.jeecms.interact.domain.dto.UserCommentDto.UpdateCheck;
import com.jeecms.interact.domain.dto.UserCommentDto.UpdateReplyt;
import com.jeecms.interact.domain.dto.UserCommentDto.UpdateStop;
import com.jeecms.interact.domain.dto.UserCommentTopDto;
import com.jeecms.interact.service.UserCommentReportService;
import com.jeecms.interact.service.UserCommentService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户评论controller层
 * 
 * @author: chenming
 * @date: 2019年5月6日 上午9:18:50
 */
@RequestMapping("/usercomment")
@RestController
public class UserCommentController extends BaseController<UserComment, Integer> {

	@Autowired
	private UserCommentService service;
	@Autowired
	private UserCommentReportService reportService;

	/**
	 * 列表分页
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = UserComment.class, includes = {"id","visitorArea","ip","createTime",
				"status","isUserDisable","isIpDisable","commentText","replytText",
				"replyUser","replyTime","content","user","isTop","isReport","reply",
				"replyAdminComment","allNum","pendingReview","successReview","errorReview"}),
		// 此处返回Id是因为在下次进行精准分页查询时需要
		@SerializeField(clazz = CoreUser.class, includes = {"username","id"}),
		@SerializeField(clazz = Content.class, includes = {"title","channel","id","urlWhole"}),
		@SerializeField(clazz = Channel.class, includes = {"name"})
	})
	public ResponseInfo page(HttpServletRequest request,Pageable pageable,
			@RequestParam(required = false) Short status,
			@RequestParam(required = false) Boolean isTop,
			@RequestParam(required = false) Boolean isReply,
			@RequestParam(required = false) Integer channelId,
			@RequestParam(required = false) Date startTime,
			@RequestParam(required = false) Date endTime,
			@RequestParam(required = false) Date replyStartTime,
			@RequestParam(required = false) Date replyEndTime,
			@RequestParam(required = false) String fuzzySearch,
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false) String commentText,
			@RequestParam(required = false) String replytText,
			@RequestParam(required = false) String title) 
					throws GlobalException {	
		Integer siteId = SystemContextUtils.getSiteId(request);
		Page<UserComment> uComments = service.findByList(
				siteId, status, isTop, isReply, channelId, startTime, endTime, replyStartTime, 
				replyEndTime, fuzzySearch, userName, ip, commentText, replytText, title, pageable);
		return new ResponseInfo(uComments);
	}

	
	/**
	 * 特定条件，列表分页
	 */
	@RequestMapping(value = "/specific/page", method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = UserComment.class, includes = {"id","visitorArea","ip","createTime","status","isUserDisable",
				"isIpDisable","commentText","replytText","replyUser","replyTime","content","user",
				"isReply", "isTop","isReport","reply","replyAdminComment"}),
		// 此处返回Id是因为在下次
		@SerializeField(clazz = CoreUser.class, includes = {"username","id"}),
		@SerializeField(clazz = Content.class, includes = {"title","channel","id","urlWhole"}),
		@SerializeField(clazz = Channel.class, includes = {"name"})
	})
	public ResponseInfo specificPage(HttpServletRequest request,Pageable pageable,
			@RequestParam(required = false) Short status,
			@RequestParam(required = false) Boolean isTop,
 			@RequestParam(required = false) Boolean isReply,
			@RequestParam(required = false) Integer channelId,
			@RequestParam(required = false) Date startTime,
			@RequestParam(required = false) Date endTime,
			@RequestParam(required = false) Date replyStartTime,
			@RequestParam(required = false) Date replyEndTime,
			@RequestParam(required = false) String fuzzySearch,
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false) String commentText,
			@RequestParam(required = false) String replytText,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) Integer contentId,
			@RequestParam(required = false) Integer userId, 
			@RequestParam(required = false) String precisionIp) 
					throws GlobalException {	
		Integer siteId = SystemContextUtils.getSiteId(request);
		Page<UserComment> uComments = service.findByList(
				siteId, status, isTop, isReply, channelId, startTime, endTime, replyStartTime, 
				replyEndTime, fuzzySearch, userName, ip, commentText, replytText, title, 
				contentId, userId, precisionIp, pageable);
		return new ResponseInfo(uComments);
	}
	
	/**
	 * 举报列表(上面的方法其实可以通用，但是改动范围太大缺乏意义，不如单独写一个方法)
	 * ps：改动太大的意思是以为列表两者操作场景不同
	 * 举报切记：举报列表完全依靠UserComment对象中的isReport进行判断与UserCommentReport集合中的数据完全无关，切记
	 */
	@RequestMapping(value = "/report/page", method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = UserComment.class, includes = {"id","visitorArea","ip","createTime","status","isUserDisable",
				"isIpDisable","commentText","replytText","replyUser","replyTime","content","user",
				"reply", "isTop", "isReport","userCommentReports","replyAdminComment"}),
		// 此处返回Id是因为在下次
		@SerializeField(clazz = CoreUser.class, includes = {"username","id"}),
		@SerializeField(clazz = Content.class, includes = {"title","channel","id","urlWhole"}),
		@SerializeField(clazz = Channel.class, includes = {"name"}),
		@SerializeField(clazz = UserCommentReport.class, includes = {"ip","user","createTime"})
	})
	public ResponseInfo reportPage(HttpServletRequest request,Pageable pageable) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Page<UserComment> uComments = service.findByList(siteId, pageable);
		return new ResponseInfo(uComments);
	}
	
	/**
	 * 修改审核状态
	 */
	@RequestMapping(value = "/status", method = RequestMethod.PUT)
	public ResponseInfo updateCheck(@RequestBody @Validated(UpdateCheck.class) UserCommentDto dto, 
			BindingResult result, HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		service.updateCheck(dto, siteId);
		return new ResponseInfo(true);
	}
	
	/**
	 * 修改回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.PUT)
	public ResponseInfo updateReply(@RequestBody @Validated(UpdateReplyt.class) UserCommentDto dto, 
			BindingResult result, HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		UserComment userComment = service.findById(dto.getId());
		// 如果站点不匹配抛出异常
		if (!userComment.getSiteId().equals(site.getId())) {
			return new ResponseInfo(SettingErrorCodeEnum.COMMENT_IS_NOT_SITE_DATA.getCode(),
					SettingErrorCodeEnum.COMMENT_IS_NOT_SITE_DATA.getDefaultMessage());
		}
		service.saveOrUpdateReply(userComment, dto, request, site);
		return new ResponseInfo(true);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels,HttpServletRequest request) 
			throws GlobalException {
		List<UserComment> uList = service.findAllById(Arrays.asList(dels.getIds()));
		Integer siteId = SystemContextUtils.getSiteId(request);
		for (UserComment userComment : uList) {
			// 检测是否是该站点数据，不是抛出异常
			if (!userComment.getSiteId().equals(siteId)) {
				return new ResponseInfo(SettingErrorCodeEnum.COMMENT_IS_NOT_SITE_DATA.getCode(),
						SettingErrorCodeEnum.COMMENT_IS_NOT_SITE_DATA.getDefaultMessage());
			}
		}
		service.deleted(uList, dels.getIds());
		return new ResponseInfo(true);
	}
	
	/**
	 * 删除评论举报
	 */
	@RequestMapping(value = "/report", method = RequestMethod.DELETE)
	public ResponseInfo reportDeleted(@RequestBody @Valid DeleteDto dels,HttpServletRequest request) 
			throws GlobalException {
		if (dels.getIds().length == 0) {
			return new ResponseInfo();
		}
		List<Integer> ids = Arrays.asList(dels.getIds());
		List<UserComment> uList = service.findAllById(ids);
		Integer siteId = SystemContextUtils.getSiteId(request);
		for (UserComment userComment : uList) {
			// 检测是否是该站点数据，不是抛出异常
			if (!userComment.getSiteId().equals(siteId)) {
				return new ResponseInfo(SettingErrorCodeEnum.COMMENT_IS_NOT_SITE_DATA.getCode(),
						SettingErrorCodeEnum.COMMENT_IS_NOT_SITE_DATA.getDefaultMessage());
			}
		}
		for (UserComment userComment : uList) {
			userComment.setIsReport(false);
		}
		service.batchUpdate(uList);
		List<UserCommentReport> reports = reportService.findByCommentId(ids);
		reportService.physicalDeleteInBatch(reports);
		return new ResponseInfo();
	}
	
	/**
	 * 新增一个禁止黑名单
	 */
	@RequestMapping(value = "/plus/stop", method = RequestMethod.PUT)
	public ResponseInfo plusStop(@RequestBody @Validated(UpdateStop.class) UserCommentDto dto, 
			BindingResult result,HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		service.saveStop(dto, site);
		return new ResponseInfo(true);
	}
	
	/**
	 * 取消一个禁止黑名单
	 */
	@RequestMapping(value = "/cancel/stop", method = RequestMethod.PUT)
	public ResponseInfo cancelStop(@RequestBody @Validated(UpdateStop.class) UserCommentDto dto, 
			BindingResult result,HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		service.deleteStop(dto, siteId);
		return new ResponseInfo(true);
	}
	
	/**
	 * 是否top
	 */
	@RequestMapping(value = "/top", method = RequestMethod.PUT)
	public ResponseInfo top(@RequestBody @Valid UserCommentTopDto dto, 
			BindingResult result,HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		service.top(dto);
		return new ResponseInfo(true);
	}
	
	/**
	 * 查询count对象
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public ResponseInfo count(@RequestParam(required = false) Integer contentId, HttpServletRequest request) 
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(service.getCount(contentId, null, siteId));
	}
	
	
}

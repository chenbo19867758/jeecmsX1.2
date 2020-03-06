package com.jeecms.front.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.ContentErrorCodeEnum;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.exception.error.SiteErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.Location;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.util.IpUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentService;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.domain.UserCommentReport;
import com.jeecms.interact.domain.dto.UserCommentSaveDto;
import com.jeecms.interact.domain.dto.UserCommentUpDto;
import com.jeecms.interact.domain.vo.UserCommentPcVo;
import com.jeecms.interact.service.UserCommentPcService;
import com.jeecms.interact.service.UserCommentReportService;
import com.jeecms.interact.service.UserCommentService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.SysBlackList;
import com.jeecms.system.service.SysBlackListService;
import com.jeecms.util.SystemContextUtils;

import net.sf.ehcache.Ehcache;

/**
 * 用户评论controller层
 *
 * @author: chenming
 * @date: 2019年5月6日 上午9:18:50
 */
@RequestMapping("/usercomment")
@RestController
@Validated
public class CommentController extends BaseController<UserComment, Integer> {

	@Autowired
	private ContentService contentService;
	@Autowired
	private UserCommentPcService service;
	@Autowired
	private SysBlackListService sListService;
	@Autowired
	private UserCommentReportService commentReportService;
	@Autowired
	private UserCommentService commentService;
	@Resource(name = CacheConstants.CONTENT_NUM)
	private Ehcache cache;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Valid UserCommentSaveDto dto, HttpServletRequest request,
							 HttpServletResponse response) throws GlobalException {
		Content content = contentService.findById(dto.getContentId());
		Integer userId = SystemContextUtils.getUserId(request);
		Integer siteId = SystemContextUtils.getSiteId(request);
		if (userId != null) {
			SysBlackList sysBlackList = sListService.findByUserIdByIp(siteId, SysBlackList.USER_COMMENT_TYPE, userId, null);
			if (sysBlackList != null) {
				return new ResponseInfo(
						UserErrorCodeEnum.THE_USER_NOT_COMMENT.getCode(),
						UserErrorCodeEnum.THE_USER_NOT_COMMENT.getDefaultMessage());
			}
		}
		dto.setUserId(userId);
		// 设置限制
		if (content == null) {
			return new ResponseInfo(
					UserErrorCodeEnum.INCOMING_CONTENT_ERROR.getCode(),
					UserErrorCodeEnum.INCOMING_CONTENT_ERROR.getDefaultMessage());
		}
		// 不允许评论抛出异常
		if (ContentConstant.COMMENT_NOT_ALLOWED == content.getCommentControl()) {
			return new ResponseInfo(
					UserErrorCodeEnum.THE_CONTENT_NOT_COMMENT.getCode(),
					UserErrorCodeEnum.THE_CONTENT_NOT_COMMENT.getDefaultMessage());
		}
		// 只允许登录评论但是没有用户id抛出异常
		if (ContentConstant.COMMENT_AFTER_LOGIN == content.getCommentControl()) {
			if (dto.getUserId() == null) {
				return new ResponseInfo(
						UserErrorCodeEnum.THE_CONTENT_NOT_TOURIST_REVIEWS.getCode(),
						UserErrorCodeEnum.THE_CONTENT_NOT_TOURIST_REVIEWS.getDefaultMessage());
			}
		}
		service.save(dto, content, request, response);
		return new ResponseInfo(true);
	}

	/**
	 * PC端评论分页
	 * @Title: getContent  
	 * @param contentId		内容ID
	 * @param pageable		分页对象
	 * @param sortStatus	1. 最热，2.最新
	 * @param request		request请求
	 * @throws GlobalException      全局异常
	 * @return: ResponseInfo
	 */
	@RequestMapping(method = RequestMethod.GET)
	@MoreSerializeField({
			// 此处返回Id是因为在下次进行精准分页查询时需要
			@SerializeField(clazz = UserComment.class, includes = {"id", "createTime",
					"commentText", "user", "children", "replyUser","upCount","replyParent",
					"isLike","report","distanceTime"}),
			@SerializeField(clazz = CoreUser.class, includes = {"username", "userExt"}),
			@SerializeField(clazz = CoreUserExt.class, includes = {"userImgUrl"})
	})
	public ResponseInfo getContent(@RequestParam Integer contentId, Pageable pageable,
								   @Range(min = 1, max = 2, message = "类型只有1或者2") @RequestParam Short sortStatus,
								   HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		CoreUser user = SystemContextUtils.getCoreUser();
		Page<UserComment> page = service.getContent(siteId, contentId, sortStatus, pageable,request,user);
		boolean isTop = sortStatus == UserComment.SORT_HOTTEST;
		Integer count = service.count(contentId, false,isTop);
		UserCommentPcVo vo = new UserCommentPcVo(page, count);
		return new ResponseInfo(vo);
	}

	
	/**
	 * 手机端评论分页-最新
	 * @Title: getMobileContent  
	 * @param contentId	内容ID
	 * @param pageable	分页信息
	 * @param request	request请求
	 * @throws GlobalException     全局异常
	 * @return: ResponseInfo
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/mobile/latest")
	@MoreSerializeField({
			@SerializeField(clazz = UserComment.class, includes = {"id", 
					"commentText", "user","upCount",
					"isLike","report","distanceTime","childrenNum"}),
			// 此处返回Id是因为在下次进行精准分页查询时需要
			@SerializeField(clazz = CoreUser.class, includes = {"username", "userExt"}),
			@SerializeField(clazz = CoreUserExt.class, includes = {"userImgUrl"})
	})
	public ResponseInfo getMobileContent(@RequestParam Integer contentId, Pageable pageable, HttpServletRequest request)
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		CoreUser user = SystemContextUtils.getCoreUser();
		Page<UserComment> page = service.getMobileContent(siteId, contentId, pageable, request, user);
		UserCommentPcVo vo = new UserCommentPcVo(page, null);
		return new ResponseInfo(vo);
	}
	
	/**
	 * 手机端评论分页-最热
	 * @Title: getMobileContent  
	 * @param contentId	内容ID
	 * @param request	request请求
	 * @throws GlobalException    全局异常  
	 * @return: ResponseInfo
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/mobile/hottest")
	@MoreSerializeField({
			@SerializeField(clazz = UserComment.class, includes = {"id", 
					"commentText", "user","upCount",
					"isLike","report","distanceTime","childrenNum"}),
			// 此处返回Id是因为在下次进行精准分页查询时需要
			@SerializeField(clazz = CoreUser.class, includes = {"username", "userExt"}),
			@SerializeField(clazz = CoreUserExt.class, includes = {"userImgUrl"})
	})
	public ResponseInfo getMobileContent(@RequestParam Integer contentId, HttpServletRequest request)
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		CoreUser user = SystemContextUtils.getCoreUser();
		List<UserComment> list = service.getMobileContent(siteId, contentId, request, user);
		UserCommentPcVo vo = new UserCommentPcVo(list, null);
		return new ResponseInfo(vo);
	}
	
	/**
	 * 查询单个评论详情
	 */
	@GetMapping("/{id}")
	@MoreSerializeField({
			@SerializeField(clazz = UserComment.class, includes = { "id", "commentText", "user",
					"children", "replyUser", "upCount", "replyParent", "isLike", "report", "distanceTime" }),
			@SerializeField(clazz = CoreUser.class, includes = { "username", "userExt" }),
			@SerializeField(clazz = CoreUserExt.class, includes = { "userImgUrl" }) 
	})
	public ResponseInfo getComment(@PathVariable(name = "id") Integer id, HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		CoreUser user = SystemContextUtils.getCoreUser();
		return new ResponseInfo(service.getComment(id, user, request, siteId));
	}
	
	
	/**
	 * 评论/回复点赞
	 *
	 * @param dto 内容id
	 * @param request   {@link HttpServletRequest}
	 * @param response  {@link HttpServletResponse}
	 * @return ResponseInfo
	 */
	@PostMapping("/up")
	public ResponseInfo like(@RequestBody @Valid UserCommentUpDto dto, HttpServletRequest request,
							 HttpServletResponse response,BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		Integer commentId = dto.getCommentId();
		if (commentId == null) {
			return new ResponseInfo(SystemExceptionEnum.INCOMPLETE_PARAM.getCode(),
					SystemExceptionEnum.INCOMPLETE_PARAM.getDefaultMessage());
		}
		CmsSite site = SystemContextUtils.getSite(request);
		CoreUser user = SystemContextUtils.getUser(request);
		String likeLogin = site.getConfig().getContentLikeLogin();
		//判断点赞是否需要登录
		if (CmsSiteConfig.TRUE_STRING.equals(likeLogin)) {
			return new ResponseInfo(SiteErrorCodeEnum.LIKE_TO_LOG_IN.getCode(),
					SiteErrorCodeEnum.LIKE_TO_LOG_IN.getDefaultMessage());
		}
		service.like(user, commentId, request, response);
		return new ResponseInfo(true);
	}

	/**
	 * 评论/回复取消点赞
	 *
	 * @param dto 内容id
	 * @param request   {@link HttpServletRequest}
	 * @param response  {@link HttpServletResponse}
	 * @return ResponseInfo
	 */
	@PostMapping("/cancel/up")
	public ResponseInfo cancelLike(@RequestBody @Valid UserCommentUpDto dto, HttpServletRequest request,
							 HttpServletResponse response,BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		Integer commentId = dto.getCommentId();
		if (commentId == null) {
			return new ResponseInfo(SystemExceptionEnum.INCOMPLETE_PARAM.getCode(),
					SystemExceptionEnum.INCOMPLETE_PARAM.getDefaultMessage());
		}
		CoreUser user = SystemContextUtils.getUser(request);
		service.cancelLike(user, commentId, request, response);
		return new ResponseInfo(true);
	}
	
	/**
	 * 举报
	 */
	@RequestMapping(value = "/report",method = RequestMethod.POST)
	public ResponseInfo report(@RequestBody @Valid UserCommentReport report,HttpServletRequest request,BindingResult result) throws GlobalException{
		super.validateBindingResult(result);
		UserComment comment = commentService.findById(report.getCommentId());
		if (comment == null) {
			return new ResponseInfo(ContentErrorCodeEnum.COMMENT_ID_PASSED_ERROR.getCode(),ContentErrorCodeEnum.COMMENT_ID_PASSED_ERROR.getDefaultMessage());
		}
		Integer userId = null;
		CoreUser user = SystemContextUtils.getUser(request);
		if (user != null) {
			userId = user.getId();
		} else {
			return new ResponseInfo(SettingErrorCodeEnum.USER_IS_NOT_LOGIN_UNABLE_TO_REPLY.getCode(),SettingErrorCodeEnum.USER_IS_NOT_LOGIN_UNABLE_TO_REPLY.getDefaultMessage());
		}
		String ip = RequestUtils.getRemoteAddr(request);
		UserCommentReport commentReport = new UserCommentReport(null, report.getCommentId(), userId, ip, comment, user);
		commentReportService.save(commentReport);
		comment.setIsReport(true);
		commentService.update(comment);
		return new ResponseInfo();
	}
	
	/**
	 * 内容的所有评论数量
	 */
	@GetMapping("/count/{contentId}")
	public ResponseInfo count(@PathVariable(name = "contentId") Integer contentId) {
		return new ResponseInfo(service.count(contentId,true,false));
	}
	

	@Autowired
	private ResourcesSpaceDataService dateService;

	@RequestMapping(method = RequestMethod.GET, value = "/a")
	public void a() {
		List<ResourcesSpaceData> datas = dateService.findAll(false);
		System.out.println("查询出来的数据的条数：" + datas.size());
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (ResourcesSpaceData a : datas) {
			String url = a.getUrl();
			url = url.substring(url.lastIndexOf("/") + 1);
			map.put(url, 1);
		}
		File file = new File("E:/a/u/cms/www/201911");
		File[] tempList = file.listFiles();
		for (File temp : tempList) {
			if (temp.isFile()) {
				if (map.get(temp.getName()) == null) {
					temp.delete();
				}
			}
		}
		System.out.println("已经结束");
	}
	
}

package com.jeecms.interact.service.impl;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.util.CookieUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.interact.dao.UserCommentDao;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.domain.UserCommentReport;
import com.jeecms.interact.domain.dto.UserCommentSaveDto;
import com.jeecms.interact.service.UserCommentPcService;
import com.jeecms.interact.service.UserCommentReportService;
import com.jeecms.interact.service.UserCommentService;
import com.jeecms.member.domain.MemberScoreDetails;
import com.jeecms.member.service.MemberScoreDetailsService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.domain.SysBlackList;
import com.jeecms.system.domain.SysSensitiveWord;
import com.jeecms.system.service.SysBlackListService;
import com.jeecms.system.service.SysSensitiveWordService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 评论PC端service实现类
 *
 * @author: chenming
 * @date: 2019年7月23日 上午9:27:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCommentPcServiceImpl implements UserCommentPcService {

	private static final String LIKE_COOKIE = "comment_like_cookie_";

	@Override
	public void save(UserCommentSaveDto dto, Content content, HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		UserComment userComment = new UserComment();
		userComment.setSiteId(site.getId());
		userComment.setContentId(dto.getContentId());
		userComment.setUserId(dto.getUserId());
		Long currentTime = System.currentTimeMillis();
		String commentTxt = this.checkConfig(dto, site, currentTime, request);
		userComment.setCommentText(commentTxt);
		String ip = RequestUtils.getRemoteAddr(request);
		// 如果ip被禁止评论抛出异常
		SysBlackList sysBlackList = sListService.findByUserIdByIp(site.getId(), SysBlackList.USER_COMMENT_TYPE, null, ip);
		if (sysBlackList != null) {
			throw new GlobalException(
					new SystemExceptionInfo(
							UserErrorCodeEnum.THE_IP_NOT_COMMENT.getDefaultMessage(),
							UserErrorCodeEnum.THE_IP_NOT_COMMENT.getCode()));
		}
		userComment.setIp(ip);
		userComment.setIsReply(false);
		if (site.getCmsSiteCfg().getCommentAudit().equals("1")) {
			userComment.setStatus(UserComment.CHECK_WAIT);
		} else {
			userComment.setStatus(UserComment.CHECK_BY);
		}
		userComment.setDownCount(0);
		userComment.setUpCount(0);
		userComment.setParentId(dto.getParentId());
		userComment.setReplyCommentId(dto.getUserCommentId());
		userComment.setIsTop(false);
		userComment.setIsReply(false);
		userComment.setIsReport(false);
		userComment.setSortNum(10);
		if (userComment.getUserId() != null) {
			userComment.setUser(coreUserService.findById(userComment.getUserId()));
		}
		userComment.setSite(site);
		userComment.setContent(content);
		UserComment bean = service.save(userComment);
		service.flush();
		// 只有在审核通过的前提下才会加入一个值
		if (UserComment.CHECK_BY.equals(userComment.getStatus())) {
			if (userComment.getUserId() != null) {
				memberScoreDetailsService.addMemberScore(MemberScoreDetails.COMMENT_SCORE_TYPE, userComment.getUserId(), site.getId(),bean.getId());
			}
			contentFrontService.saveOrUpdateNum(dto.getContentId(), null, ContentConstant.CONTENT_NUM_TYPE_COMMENTS, false);
		}
		if (userComment.getUserId() != null) {
			cacheProvider.setCache(UserComment.USER_COMMENT_CACHE_KEY, String.valueOf(userComment.getUserId()), currentTime);
		} else {
			sessionProvider.setAttribute(request, UserComment.USER_COMMENT_TIME_INTERVAL, currentTime);
		}
	}

	/**
	 * 校验站点配置的信息，和全局配置的信息(敏感词)
	 */
	private String checkConfig(UserCommentSaveDto dto, CmsSite site, Long currentTime,
							   HttpServletRequest request) throws GlobalException {
		Long limitInterval = Long.valueOf(site.getCmsSiteCfg().getCommentCycle());
		Long oldTime = 0L;
		if (dto.getUserId() != null) {
			if (cacheProvider.exist(UserComment.USER_COMMENT_CACHE_KEY, String.valueOf(dto.getUserId()))) {
				oldTime = Long.valueOf(String.valueOf(cacheProvider.getCache(UserComment.USER_COMMENT_CACHE_KEY, String.valueOf(dto.getUserId()))));
			}
		} else {
			String oldTimeStr = String.valueOf(sessionProvider.getAttribute(request, UserComment.USER_COMMENT_TIME_INTERVAL));
			if (StringUtils.isNotBlank(oldTimeStr) && !oldTimeStr.equals("null")) {
				oldTime = Long.valueOf(oldTimeStr);
			}
		}
		if ((currentTime - oldTime) / 1000 < limitInterval) {
			throw new GlobalException(
					new SystemExceptionInfo(
							"在" + limitInterval + "秒内无法继续发送",
							UserErrorCodeEnum.THE_COMMENT_UNABLE_TO_SEND.getCode()));
		}
		String commentTxt = dto.getCommentText();
		List<String> urls = this.checkUrl(commentTxt);
		if (site.getCmsSiteCfg().getCommentAllowedLink()) {
			commentTxt = commentTxt.toLowerCase();
			String commentLink = site.getCmsSiteCfg().getCommentLink();
			if (StringUtils.isNotBlank(commentLink)) {
				for (String string : urls) {
					commentTxt = commentTxt.replaceAll(string, commentLink);
				}
			}
		} else {
			if (urls.size() > 0) {
				throw new GlobalException(
						new SystemExceptionInfo(
								UserErrorCodeEnum.THE_COMMENT_CONTAIN_LINK.getDefaultMessage(),
								UserErrorCodeEnum.THE_COMMENT_CONTAIN_LINK.getCode()));
			}
		}
		commentTxt = this.checkWords(commentTxt, request);
		return commentTxt;
	}

	/**
	 * 校验敏感词问题
	 */
	private String checkWords(String commentTxt, HttpServletRequest request) throws GlobalException {
		List<SysSensitiveWord> words = sensitiveWordService.findAll(true);
		GlobalConfigAttr configAttr = SystemContextUtils.getGlobalConfig(request).getConfigAttr();
		if (words != null && words.size() > 0) {
			Map<String,String> wordMap = new HashMap<String, String>();
			for (SysSensitiveWord word : words) {
				wordMap.put(word.getSensitiveWord(), word.getReplaceWord());
			}
			// "0"-false,"1"-true
			if (configAttr.getIsSensitiveWords().equals("1")) {
				/*
				 * 1. 敏感词实体中存在"替换词"，则直接用于替换
				 * 2. 敏感词实体中不存在"替换词"，且系统配置中"敏感词默认替换"不为空，直接将"敏感词默认替换"用于替换
				 * 3. 敏感词实体中不存在"替换词"，且系统配置中"敏感词默认替换"为空，不替换
				 */
				String replaceWord = null;
				for (String sensitiveWord : wordMap.keySet()) {
					replaceWord = wordMap.get(sensitiveWord);
					if (StringUtils.isNotBlank(replaceWord)) {
						commentTxt =  commentTxt.replaceAll(sensitiveWord, replaceWord);
					} else {
						replaceWord = configAttr.getSensitiveWordsReplace();
						if (StringUtils.isNotBlank(replaceWord)) {
							commentTxt =  commentTxt.replaceAll(sensitiveWord, replaceWord);
						}
					}
				}
			} else {
				for (String sensitiveWord : wordMap.keySet()) {
					if (commentTxt.indexOf(sensitiveWord) != -1) {
						throw new GlobalException(
								new SystemExceptionInfo(
										UserErrorCodeEnum.THE_COMMENT_CONTAIN_SENSITIVE_WORD.getDefaultMessage(),
										UserErrorCodeEnum.THE_COMMENT_CONTAIN_SENSITIVE_WORD.getCode()));
					}
				}
			}
		}
		return commentTxt;
	}

	/**
	 * 检验评论内容中是否包含url，包含则返回包含的URL
	 */
	private List<String> checkUrl(String contentTxt) {
		contentTxt = contentTxt.replaceAll("[\u4E00-\u9FA5]", "#");
		contentTxt = contentTxt.replaceAll(",", "#");
		contentTxt = contentTxt.replaceAll("，", "#");
		List<String> urls = new ArrayList<String>();
		String url[] = contentTxt.split("#");
		
		// 转换为小写
		if (url != null && url.length > 0) {
			for (String tempurl : url) {
				if (StringUtils.isBlank(tempurl)) {
					continue;
				}
				tempurl = tempurl.toLowerCase();
				String regex = "^((https|http|ftp|rtsp|mms)?://)"
						+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
						+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
						+ "|" // 允许IP和DOMAIN（域名）
						+ "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
						+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
						+ "[a-z]{2,6})" // first level domain- .com or .museum
						+ "(:[0-9]{1,4})?" // 端口- :80
						+ "((/?)|" // a slash isn't required if there is no file name
						+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
				Pattern p = Pattern.compile(regex);
				Matcher matcher = p.matcher(tempurl);
				if (matcher.find()) {
					urls.add(tempurl);
				}
			}
		}
		return urls;
	}

	@Override
	public Page<UserComment> getContent(Integer siteId, Integer contentId, Short sortStatus, Pageable pageable,HttpServletRequest request, CoreUser user) 
			throws GlobalException {
		Page<UserComment> commentPage = dao.getPcPage(siteId, contentId, sortStatus, false,  pageable);
		// 用户点赞的评论集合
		List<Integer> likeCommentIds = this.getCommentIds(true, user);
		// 用户举报的评论集合
		List<Integer> reportCommentIds = this.getCommentIds(false, user);
		
		if (commentPage != null && commentPage.getSize() > 0) {
			for (UserComment userComment : commentPage) {
				List<UserComment> commentChildren = userComment.getChildren();
				if (commentChildren != null && commentChildren.size() > 0) {
					// 排除子集中审核不通过的子集
					commentChildren = commentChildren.stream()
							.filter(comment -> UserComment.CHECK_BY.equals(comment.getStatus()))
							.collect(Collectors.toList());
					userComment.setChildren(commentChildren);
					for (UserComment children : commentChildren) {
						children = this.processFunction(children, user, likeCommentIds, reportCommentIds, request);
					}
				}
				userComment = this.processFunction(userComment, user, likeCommentIds, reportCommentIds, request);
			}
		}
		return commentPage;
	}

	/**
	 * 获取评论id值(点赞、举报)
	 */
	private List<Integer> getCommentIds(boolean isLike, CoreUser user) {
		List<Integer> ids = new ArrayList<Integer>();
		if (user != null) {
			if (isLike) {
				if (user.getLikeComments() != null && user.getLikeComments().size() > 0) {
					ids = user.getLikeComments().stream().map(UserComment::getId).collect(Collectors.toList());
				}
			} else {
				List<UserCommentReport> reports = reportService.findByReplyUserId(user.getId());
				if (reports != null && reports.size() > 0) {
					ids = reports.stream().map(UserCommentReport::getCommentId).collect(Collectors.toList());
				}
			}
		}
		return ids;
	}
	
	/**
	 * 处理功能(处理评论的点赞和举报功能)
	 */
	private UserComment processFunction(UserComment userComment,CoreUser user,List<Integer> likeCommentIds, List<Integer> reportCommentIds,HttpServletRequest request) {
		// 如果用户已登录存在，处理该评论是否点赞和举报
		if (user != null) {
			userComment.setIsLike(!likeCommentIds.contains(userComment.getId()));
			userComment.setReport(!reportCommentIds.contains(userComment.getId()));
		} else {
			// 如果用户未登录，通过cookie进行判断其是否点赞，用户未登录无法举报
			userComment.setIsLike(this.isLike(userComment.getId(), request));
		}
		return userComment;
	}
	
	@Override
	public Page<UserComment> getMobileContent(Integer siteId, Integer contentId, Pageable pageable,
			HttpServletRequest request, CoreUser user) throws GlobalException {
		Page<UserComment> commentPage = dao.getPcPage(siteId, contentId, null, true, pageable);
		// 用户点赞的评论集合
		List<Integer> likeCommentIds = this.getCommentIds(true, user);
		// 用户举报的评论集合
		List<Integer> reportCommentIds = this.getCommentIds(false, user);
		if (commentPage != null && commentPage.getSize() > 0) {
			for (UserComment userComment : commentPage) {
				userComment = this.processFunction(userComment, user, likeCommentIds, reportCommentIds, request);
			}
		}
		return commentPage;
	}
	
	@Override
	public List<UserComment> getMobileContent(Integer siteId, Integer contentId,
			HttpServletRequest request, CoreUser user) throws GlobalException {
		List<UserComment> commentList = dao.getList(siteId, contentId);
		// 用户点赞的评论集合
		List<Integer> likeCommentIds = this.getCommentIds(true, user);
		// 用户举报的评论集合
		List<Integer> reportCommentIds = this.getCommentIds(false, user);
		if (commentList != null && commentList.size() > 0) {
			for (UserComment userComment : commentList) {
				userComment = this.processFunction(userComment, user, likeCommentIds, reportCommentIds, request);
			}
		}
		return commentList;
	}
	
	@Override
	public UserComment getComment(Integer id, CoreUser user, HttpServletRequest request, Integer siteId) throws GlobalException {
		UserComment userComment = service.findById(id);
		if (userComment == null) {
			return null;
		}
		if (!userComment.getSiteId().equals(siteId)) {
			return null;
		}
		// 用户点赞的评论集合
		List<Integer> likeCommentIds = this.getCommentIds(true, user);
		// 用户举报的评论集合
		List<Integer> reportCommentIds = this.getCommentIds(false, user);
		List<UserComment> commentChildren = userComment.getChildren();
		if (commentChildren != null && commentChildren.size() > 0) {
			// 排除子集中审核不通过的子集
			commentChildren = commentChildren.stream()
					.filter(comment -> UserComment.CHECK_BY.equals(comment.getStatus())).collect(Collectors.toList());
			userComment.setChildren(commentChildren);
			for (UserComment children : commentChildren) {
				children = this.processFunction(children, user, likeCommentIds, reportCommentIds, request);
			}
		}
		userComment = this.processFunction(userComment, user, likeCommentIds, reportCommentIds, request);
		return userComment;
	}
	
	/**
	 * 是否点赞
	 */
	private Boolean isLike(Integer commentId,HttpServletRequest request) {
		String cookieName = LIKE_COOKIE + commentId;
		Cookie cookie = CookieUtils.getCookie(request, cookieName);
		String cookieValue;
		if (cookie != null && !org.apache.commons.lang.StringUtils.isBlank(cookie.getValue())) {
			cookieValue = cookie.getValue();
		} else {
			cookieValue = null;
		}
		//如果cookieValue为空表示可以点赞
		if (cookieValue == null) {
			return true;
		}
		return false;
	}
	
	@Override
	public void like(CoreUser user, Integer commentId, HttpServletRequest request,
					 HttpServletResponse response) throws GlobalException {
		UserComment comment = service.findById(commentId);
		comment.setUpCount(comment.getUpCount() + 1);
		comment = service.update(comment);
		if (user != null) {
			List<UserComment> list = user.getLikeComments();
			if (!list.contains(comment)) {
				list.add(comment);
				user.setLikeComments(list);
				userService.update(user);
			}
		} else {
			String cookieName = LIKE_COOKIE + commentId;
			Cookie cookie = CookieUtils.getCookie(request, cookieName);
			String cookieValue;
			if (cookie != null && !org.apache.commons.lang.StringUtils.isBlank(cookie.getValue())) {
				cookieValue = cookie.getValue();
			} else {
				cookieValue = null;
			}
			//如果cookieValue为空表示可以点赞
			if (cookieValue == null) {
				cookieValue = org.apache.commons.lang.StringUtils.remove(UUID.randomUUID().toString(), "-");
				CookieUtils.addCookie(request, response, cookieName, cookieValue, Integer.MAX_VALUE, null);
			}
		}
	}

	@Override
	public void cancelLike(CoreUser user, Integer commentId, HttpServletRequest request,
						   HttpServletResponse response) throws GlobalException {
		UserComment comment = service.findById(commentId);
		if (user == null) {
			//未登录删除点赞cookie
			String cookieName = LIKE_COOKIE + commentId;
			Cookie cookie = CookieUtils.getCookie(request, cookieName);
			if (null != cookie && !org.apache.commons.lang.StringUtils.isBlank(cookie.getValue())) {
				CookieUtils.cancleCookie(request, response, cookieName, null);
				comment.setUpCount(comment.getUpCount() - 1);
				service.update(comment);
			}
		} else {
			//登录删除点赞数据
			List<UserComment> list = user.getLikeComments();
			if (list.contains(comment)) {
				list.remove(comment);
				comment.setUpCount(comment.getUpCount() - 1);
				service.update(comment);
			}
			user.setLikeComments(list);
			userService.update(user);
		}
	}

	@Override
	public Integer count(Integer contentId,boolean isAll,boolean isTop) {
		return Integer.valueOf(dao.getCount(contentId,isAll,isTop)+"");
	}
	
	@Autowired
	private SysSensitiveWordService sensitiveWordService;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private CacheProvider cacheProvider;
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private UserCommentService service;
	@Autowired
	private UserCommentDao dao;
	@Autowired
	private SysBlackListService sListService;
	@Autowired
	private CoreUserService userService;
	@Autowired
	private ContentFrontService contentFrontService;
	@Autowired
	private UserCommentReportService reportService;
	@Autowired
	private MemberScoreDetailsService memberScoreDetailsService;

	

}

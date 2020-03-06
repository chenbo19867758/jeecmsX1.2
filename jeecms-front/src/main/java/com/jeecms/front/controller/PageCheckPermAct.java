package com.jeecms.front.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.common.web.util.UrlUtil;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentService;
import com.jeecms.member.domain.MemberGroup;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.util.SystemContextUtils;

/**
 * 检查权限
 */
@RestController
public class PageCheckPermAct {

	/**
	 * 正常状态，可浏览
	 */
	private static final Short STATUS_NORMAL = 1;
	/**
	 * 用户未登录，需要登录
	 */
	private static final Short STATUS_NOT_LOGIN = 2;
	/**
	 * 浏览权限不足
	 */
	private static final Short STATUS_PERM_LIMIT = 3;
	/**
	 * 用户密级不足
	 */
	private static final Short STATUS_SECRET_LIMIT = 4;

	/**
	 * 检查权限( 正常 data =1 为2则是未登录 为3则是无权限浏览)
	 * 
	 * @Title: checkPerm
	 * @param contentId
	 *            内容Id
	 * @param channelId
	 *            栏目ID
	 * @param request
	 *            HttpServletRequest
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/permCheck")
	public ResponseInfo checkPerm(Integer contentId, Integer channelId, HttpServletRequest request,
			HttpServletResponse response) {
		/** 无需登录正常情况下 1 */
		Short result = STATUS_NORMAL;
		CoreUser user = SystemContextUtils.getUser(request);
		CmsSite site = SystemContextUtils.getSite(request);
		String header = RequestUtils.getHeaderOrParam(request, redirectHeader);
		/** 站点关闭且非资源等开放请求URL */
		if (!site.getIsOpen() && !UrlUtil.isOpenRequest(request)) {
			/**需要前端处理根据重定向页面*/
			if (StringUtils.isNoneBlank(header) && "false".equals(header.toLowerCase())) {
				String msg = MessageResolver.getMessage(SystemExceptionEnum.SITE_CLOSE.getCode(),
						SystemExceptionEnum.SITE_CLOSE.getDefaultMessage());
				String code = SystemExceptionEnum.SITE_CLOSE.getCode();
				String siteCloseUrl = WebConstants.SITE_CLOSE;
				if (StringUtils.isNoneBlank(site.getContextPath())) {
					siteCloseUrl = site.getContextPath() + siteCloseUrl;
				}
				ResponseInfo responseInfo = new ResponseInfo(code, msg, request, siteCloseUrl, null);
				ResponseUtils.renderJson(response, JSON.toJSONString(responseInfo));
			}
		}
		if (contentId != null) {
			Content content = contentService.findById(contentId);
			if (content != null) {
				Short viewControl = content.getRealViewControl();
				/** 先检查浏览权限设置 */
				result = getCheckPermResult(viewControl, content.getChannelId(),content.getUserId(), user);
				GlobalConfig config = SystemContextUtils.getGlobalConfig(request);
				if (STATUS_NORMAL.equals(result)) {
					/** 检查密级要求设置 */
					Short secretRes = getCheckSecretResult(content, user, config);
					if (STATUS_NORMAL.equals(secretRes)) {
						result = secretRes;
					}
				}
			}
		}
		if (channelId != null) {
			Channel channel = channelService.findById(channelId);
			if (channel != null) {
				Short viewControl = channel.getViewControl();
				result = getCheckPermResult(viewControl, channel.getId(),null, user);
			}
		}
		return new ResponseInfo(SystemExceptionEnum.SUCCESSFUL.getCode(),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage(), result);
	}

	private Short getCheckSecretResult(Content content, CoreUser user, GlobalConfig config) {
		Short result = STATUS_NORMAL;
		/** 系统是否开启了密级 */
		if (config.getConfigAttr().getOpenContentSecurity()) {
			boolean existSecretField = content.getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_SECRET);
			/** 内容密级信息存在且模型字段存在则需要校验密级，用户未登录则需要登录 */
			if (existSecretField && content.getContentSecretId() != null) {
				if (user == null) {
					result = STATUS_NOT_LOGIN;
				} else {
					/** 用户没有密级信息 则无权浏览 */
					if (user.getUserSecret() == null) {
						result = STATUS_SECRET_LIMIT;
					} else {
						/** 用户的内容密级权限未包含内容的密级要求则认定无权浏览 */
						if (!user.getUserSecret().getContentSecretIds().contains(content.getContentSecretId())) {
							result = STATUS_SECRET_LIMIT;
						}
					}
				}
			}
			/** 若开启密级功能后但未启用此字段，判定内容不设置密级，所有人都能看； */
		}
		return result;
	}

	private Short getCheckPermResult(Short viewControl, Integer channelId, Integer contentCreateUserId,CoreUser user) {
		Short result = STATUS_NORMAL;
		/** 需要登录 */
		if (!SysConstants.VISIT_LOGIN_NO_LIMIT.equals(viewControl)) {
			if (user == null) {
				/** 用户未登录 */
				result = STATUS_NOT_LOGIN;
			} else {
				/** 浏览权限限制只对非管理员且非撰写者有效 */
				if (!user.getAdmin()&&!user.getId().equals(contentCreateUserId)) {
					MemberGroup userGroup = user.getUserGroup();
					if (userGroup != null) {
						/** 会员组非所有栏目权限 则需要检查栏目权限 */
						if (userGroup.getIsAllChannelView() == null || !userGroup.getIsAllChannelView()) {
							List<Channel> channels = userGroup.getViewChannels();
							/** 会员组设置的浏览权限未包含当前栏目则认定为无权限 */
							if (!Channel.fetchIds(channels).contains(channelId)) {
								result = STATUS_PERM_LIMIT;
							}
						}
					}
					/** 用户没有归属会员组则默认可以浏览 */
				}
			}
		}
		return result;
	}

	@Value("${redirect.header}")
	private String redirectHeader;
	@Autowired
	private ContentService contentService;
	@Autowired
	private ChannelService channelService;
}

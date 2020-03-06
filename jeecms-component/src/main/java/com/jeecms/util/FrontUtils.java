package com.jeecms.util;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.util.FrontUtilBase;
import com.jeecms.common.util.PropertiesUtil;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.member.domain.vo.MemberVo;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.GlobalConfigDTO;
import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static com.jeecms.common.constants.TplConstants.TPLDIR_COMMON;
import static com.jeecms.common.constants.TplConstants.TPLDIR_MEMBER;
import static com.jeecms.common.constants.TplConstants.TPLDIR_STYLE_LIST;
import static com.jeecms.common.constants.TplConstants.TPL_STYLE_PAGE_CHANNEL;
import static com.jeecms.common.constants.TplConstants.TPL_SUFFIX;
import static com.jeecms.common.constants.WebConstants.UTF8;
import static com.jeecms.common.web.filter.HeaderCorsFilter.START_TIME;

/**
 * 前台工具类基类
 *
 * @author: tom
 * @date: 2018年9月14日 下午7:05:05
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FrontUtils extends FrontUtilBase {
	private static final Logger log = LoggerFactory.getLogger(FrontUtils.class);
	private static Properties SystemProperty;

	static {
		try {
			SystemProperty = PropertiesUtil.loadSystemProperties();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 获得模板路径。将对模板文件名称进行本地化处理。
	 *
	 * @param request HttpServletRequest
	 * @param dir     模板目录。不本地化处理。
	 * @param name    模板名称。本地化处理。
	 * @return
	 */
	public static String getTplPath(HttpServletRequest request, String dir, String name) {
		String solution = getSolutionPath(request);
		String tpl = solution + "/";
		if (StringUtils.isNoneBlank(dir)) {
			tpl += dir + "/";
		}
		tpl += name + TPL_SUFFIX;
		return tpl;
	}

	/**
	 * 获得模板路径。不对模板文件进行本地化处理。
	 *
	 * @param solution 方案路径
	 * @param dir      模板目录。不本地化处理。
	 * @param name     模板名称。不本地化处理。
	 * @return
	 */
	public static String getTplPath(String solution, String dir, String name) {
		return solution + "/" + dir + "/" + name + TPL_SUFFIX;
	}

	/**
	 * 获取模板路径
	 *
	 * @param request HttpServletRequest
	 * @param name    模板名称
	 * @Title: getTplPath
	 * @return: String
	 */
	public static String getTplPath(HttpServletRequest request, String name) {
		String solution = getSolutionPath(request);
		return solution + "/" + name + TPL_SUFFIX;
	}

	/**
	 * 获取当前访问站点模板方案
	 *
	 * @param request HttpServletRequest
	 * @Title: getSolution
	 * @return: String
	 */
	public static String getSolutionPath(HttpServletRequest request) {
		CmsSite site = SystemContextUtils.getSite(request);
		String solution = site.getPcSolutionPath();
		if (SystemContextUtils.isMobile() || SystemContextUtils.isTablet()) {
			solution = site.getMobileSolutionPath();
		}
		/*if (SystemContextUtils.isTablet()) {
			solution = site.getTabletSolutionPath();
		}*/
		return solution;
	}

	/**
	 * 获取模板绝对路径
	 *
	 * @param request   HttpServletRequest
	 * @param name      名称
	 * @param separator 分隔符
	 * @Title: getTplAbsolutePath
	 * @return: String
	 */
	public static String getTplAbsolutePath(HttpServletRequest request, String name, String separator) {
		if (StringUtils.isNoneBlank(name)) {
			String path = "";
			String[] paths = name.split(separator);
			for (String p : paths) {
				path += "/" + p;
			}
			/** 截去第一个/ */
			path = path.substring(1);
			/** _5后缀是数字，则认定是分页信息，则截去，_5p 普通字符串则是模板名称 */
			int lastPageSpt = path.indexOf("_");
			if (lastPageSpt != -1) {
				String lastStr = path.substring(lastPageSpt + 1);
				if (StringUtils.isNoneBlank(lastStr) && StringUtils.isNumeric(lastStr)) {
					path = path.substring(0, lastPageSpt);
				}
			}
			return path;
		} else {
			return getTplPath(request, TPLDIR_MEMBER, "login");
		}
	}

	/**
	 * 页面没有找到
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return 返回“页面没有找到”的模板
	 */
	public static String pageNotFound(HttpServletRequest request, HttpServletResponse response,
									  Map<String, Object> model) {
		frontData(request, model);
		return getTplPath(request, TPLDIR_COMMON, PAGE_NOT_FOUND);
	}

	/**
	 * 返回系统错误模板
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param model    ModelMap
	 * @Title: systemError
	 * @return: String
	 */
	public static String systemError(HttpServletRequest request, HttpServletResponse response,
									 Map<String, Object> model) {
		frontData(request, model);
		return getTplPath(request, TPLDIR_COMMON, ERROR_PAGE);
	}

	/**
	 * 返回站点关闭模板
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param model    ModelMap
	 * @Title: showSiteClosePage
	 * @return: String
	 */
	public static String showSiteClosePage(HttpServletRequest request, HttpServletResponse response,
										   Map<String, Object> model) {
		frontData(request, model);
		return getTplPath(request, TPLDIR_COMMON, SITE_CLOSE_PAGE);
	}

	/**
	 * 为前台模板设置公用数据
	 *
	 * @param request HttpServletRequest
	 * @param map     ModelMap
	 */
	public static void frontData(HttpServletRequest request, Map<String, Object> map) {
		CoreUser user = SystemContextUtils.getUser(request);
		CmsSite site = SystemContextUtils.getSite(request);
		Long startTime = (Long) request.getAttribute(START_TIME);
		GlobalConfigDTO config = SystemContextUtils.getResponseConfigDto(request);
		frontData(map, site, user, startTime, config);
	}

	/**
	 * 设置前台（含PC端及移动端）模板公共数据
	 *
	 * @Title: frontData
	 * @param: map
	 * ModelMap
	 * @param: startTime
	 * 开始请求时间
	 * @return: void
	 */
	public static void frontData(Map<String, Object> map, CmsSite site, CoreUser user, Long startTime,
								 GlobalConfigDTO config) {
		if (startTime != null) {
			map.put(START_TIME, startTime);
		}
		String ctx =  "" ;
		if(site!=null){
			if (StringUtils.isNotBlank(site.getContextPath())) {
				ctx = site.getContextPath();
			}
		}
		map.put(BASE, ctx);
		String freemarkerLoaderPath = null;
		if (SystemProperty != null) {
			freemarkerLoaderPath = SystemProperty.getProperty("freemarker.templateLoaderPath");
		}
		if(site!=null){
			String resTpl = ctx + site.getResPath();
			String mobileResTpl = ctx + site.getMobileResPath();
			if (StringUtils.isNoneBlank(freemarkerLoaderPath)) {
				resTpl = freemarkerLoaderPath + resTpl;
				mobileResTpl = freemarkerLoaderPath + mobileResTpl;
			}
			map.put(RES_TPL, resTpl);
			map.put(MOBILE_RES_TPL, mobileResTpl);
		}
		map.put(SITE, site);
		// 当前登录用户信息
		map.put(USER, filterMember(user));
		// 获取对外输出的系统配置，过滤掉关键数据
		map.put(GLOBAL_CONFIG, config);
	}

	/**
	 * 标签中获得站点
	 *
	 * @param env
	 * @return
	 * @throws TemplateModelException
	 */
	public static CmsSite getSite(Environment env)
			throws TemplateModelException {
		TemplateModel model = env.getGlobalVariable(SITE);
		if (model instanceof AdapterTemplateModel) {
			return (CmsSite) ((AdapterTemplateModel) model)
					.getAdaptedObject(CmsSite.class);
		} else {
			throw new TemplateModelException("'" + SITE
					+ "' not found in DataModel");
		}
	}

	/**
	 * 会员信息(提供前台模板获取使用，过滤掉关键信息，如密码、支付密码)
	 *
	 * @Title: filterMember
	 * @param: @param
	 * member
	 * @param: @return
	 * @return: MemberVo
	 */
	private static MemberVo filterMember(CoreUser member) {
		if (member != null) {
			MemberVo vo = new MemberVo();
			vo.setId(member.getId());
			vo.setUsername(member.getUsername());
			vo.setEmail(member.getEmail());
			vo.setEnabled(member.getEnabled());
			vo.setAdmin(member.getAdmin());
			return vo;
		}
		return null;
	}

	public static void includePagination(CmsSite site,
										 Map<String, TemplateModel> params, Environment env)
			throws TemplateException, IOException {
		String sysPage = DirectiveUtils.getString(PARAM_SYS_PAGE, params);
		String userPage = DirectiveUtils.getString(PARAM_USER_PAGE, params);
		if (!org.apache.commons.lang.StringUtils.isBlank(sysPage)) {
			String tpl = TPL_STYLE_PAGE_CHANNEL + sysPage + TPL_SUFFIX;
			env.include(tpl, UTF8, true);
		} else if (!org.apache.commons.lang.StringUtils.isBlank(userPage)) {
			String tpl = getTplPath(site.getSolutionPath(), TPLDIR_STYLE_LIST,
					userPage);
			env.include(tpl, UTF8, true);
		}
	}

}

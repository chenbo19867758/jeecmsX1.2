package com.jeecms.common.util;

import static com.jeecms.common.constants.TplConstants.TPL_SUFFIX;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.UrlHelper;
import com.jeecms.common.web.util.UrlHelper.PageInfo;

import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

/**
 * 前台工具类
 * @author: tom
 * @date:   2018年12月26日 下午3:00:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FrontUtilBase {
	/**
	 * 页面没有找到
	 */
	public static final String PAGE_NOT_FOUND = "page_not_found";
	/**
	 * 操作失败页面
	 */
	public static final String ERROR_PAGE = "error_page";
	/**
	 * 站点关闭提示页面
	 */
	public static final String SITE_CLOSE_PAGE = "site_close_page";
	/**
	 * 模板资源路径
	 */
	public static final String RES_TPL = "res";
	/**
	 * 模板资源表达式
	 */
	public static final String RES_EXP = "${res}";
	/**
	 * 手机模板资源路径
	 */
	public static final String MOBILE_RES_TPL = "mobileRes";
	/**
	 * 部署路径
	 */
	public static final String BASE = "base";
	/**
	 * 站点
	 */
	public static final String SITE = "site";
	/**
	 * 全局配置
	 */
	public static final String GLOBAL_CONFIG = "globalConfig";

	/**
	 * 全局csrfToken
	 */
	public static final String CSRF_TOKEN = "csrfToken";
	/**
	 * 用户
	 */
	public static final String USER = "user";
	/**
	 * 页码
	 */
	public static final String PAGE_NO = "pageNo";
	/**
	 * 总条数
	 */
	public static final String COUNT = "count";
	/**
	 * 起始条数
	 */
	public static final String FIRST = "first";

	/**
	 * 页面翻页地址
	 */
	public static final String HREF = "href";
	/**
	 * href前半部（相对于分页）
	 */
	public static final String HREF_FORMER = "hrefFormer";
	/**
	 * href后半部（相对于分页）
	 */
	public static final String HREF_LATTER = "hrefLatter";
	/**
	 * 页面完整地址
	 */
	public static final String LOCATION = "location";

	/**
	 * 传入参数，列表样式。
	 */
	public static final String PARAM_STYLE_LIST = "styleList";
	/**
	 * 传入参数，系统预定义翻页。
	 */
	public static final String PARAM_SYS_PAGE = "sysPage";
	/**
	 * 传入参数，用户自定义翻页。
	 */
	public static final String PARAM_USER_PAGE = "userPage";

	/**
	 * 返回页面
	 */
	public static final String RETURN_URL = "returnUrl";

	/**
	 * 国际化参数
	 */
	public static final String ARGS = "args";

	public static final int MAX_COUNT = 5000;

	public static String getSysPagePath(HttpServletRequest request, String dir, String name) {
		return SysConstants.TPL_BASE + "/" + dir + "/" + name + TPL_SUFFIX;
	}

	/**
	 * 为前台模板设置分页相关数据
	 * @param request HttpServletRequest
	 * @param map Map
	 */ 
	public static void frontPageData(HttpServletRequest request, Map<String, Object> map) {
		int pageNo = UrlHelper.getPageNo(request);
		PageInfo info = UrlHelper.getPageInfo(request);
		String href = info.getHref();
		String hrefFormer = info.getHrefFormer();
		String hrefLatter = info.getHrefLatter();
		frontPageData(pageNo, href, hrefFormer, hrefLatter, map);
	}

	/**
	 * 为前台模板设置分页相关数据
	 * @param pageNo 分页
	 * @param href url
	 * @param hrefFormer url前缀
	 * @param hrefLatter url后缀
	 * @param map 参数
	 */
	public static void frontPageData(int pageNo, String href, String hrefFormer, String hrefLatter,
			Map<String, Object> map) {
		href = java.text.Normalizer.normalize(href, java.text.Normalizer.Form.NFKD);
		hrefFormer = java.text.Normalizer.normalize(hrefFormer, java.text.Normalizer.Form.NFKD);
		hrefLatter = java.text.Normalizer.normalize(hrefLatter, java.text.Normalizer.Form.NFKD);
		map.put(PAGE_NO, pageNo);
		map.put(HREF, href);
		map.put(HREF_FORMER, hrefFormer);
		map.put(HREF_LATTER, hrefLatter);
	}
	
	public static void putLocation(Map<String, Object> map, String location) {
		map.put(LOCATION, location);
	}

	/**
	 * 优先URL地址中获得分页 ，其次从request中获取参数
	 * 
	 * @param env  Environment
	 * @throws TemplateException TemplateException
	 */
	public static int getPageNo(Environment env) throws TemplateException {
		TemplateModel pageNo = env.getGlobalVariable(PAGE_NO);
		if (pageNo instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) pageNo).getAsNumber().intValue();
		} else {
			HttpServletRequest request = RequestUtils.getHttpServletRequest();
			String pageNoStr = request.getParameter(PAGE_NO);
			if (StringUtils.isNoneBlank(pageNoStr) && StringUtils.isNumeric(pageNoStr)) {
				return Integer.parseInt(pageNoStr);
			}
			throw new TemplateModelException("'" + PAGE_NO + "' not found in DataModel.");
		}
	}

	/**
	 * 优先标签参数中获得页码，其次从request中获取参数
	 * @Title: getPageNo
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @return: int
	 */
	public static int getPageNo(Map<String, TemplateModel> params) throws TemplateException {
		Integer count = DirectiveUtils.getInt(PAGE_NO, params);
		if (count != null) {
			return count;
		}
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String pageNoStr = request.getParameter(PAGE_NO);
		if (StringUtils.isNoneBlank(pageNoStr) && StringUtils.isNumeric(pageNoStr)) {
			return Integer.parseInt(pageNoStr);
		}
		throw new TemplateModelException("'" + PAGE_NO + "' not found in DataModel.");
	}

	/**
	 * 获取first
	 * @Title: getFirst  
	 * @param params Map
	 * @throws TemplateException   TemplateException    
	 * @return: int
	 */
	public static int getFirst(Map<String, TemplateModel> params) throws TemplateException {
		Integer first = DirectiveUtils.getInt(FIRST, params);
		if (first == null || first <= 0) {
			return 0;
		} else {
			return first - 1;
		}
	}

	/**
	 * 标签参数中获得条数。
	 * @param params Map
	 * @return 如果不存在，或者小于等于0，或者大于5000则返回5000；否则返回条数。
	 * @throws TemplateException TemplateException
	 */
	public static int getCount(Map<String, TemplateModel> params) throws TemplateException {
		Integer count = DirectiveUtils.getInt(COUNT, params);
		if (count == null || count <= 0 || count >= MAX_COUNT) {
			return MAX_COUNT;
		} else {
			return count;
		}
	}
	

}

package com.jeecms.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.Area;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.dto.GlobalConfigDTO;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.FrontUtils;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 前端公共控制器，用于跳转普通视图
 * 
 * @author: tom
 * @date: 2018年9月7日 下午4:27:57
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Controller
public class FrontCommonController {

	@RequestMapping(value = "/")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		FrontUtils.frontData(request, model);
		return FrontUtils.getTplPath(request, null, WebConstants.INDEX);
	}

	/**
	 * 通用页面跳转
	 * 
	 * @Title: page
	 * @param page
	 *            访问url
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param model
	 *            ModelMap
	 * @throws Exception
	 *             Exception
	 * @return String
	 */
	@RequestMapping(value = "/{page}.htm")
	public String page(@PathVariable String page, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		String loginUrl = WebConstants.LOGIN_URL;
		String ctx = request.getContextPath();
		if (StringUtils.isNoneBlank(ctx)) {
			loginUrl = ctx + loginUrl;
		}
		FrontUtils.frontData(request, model);
		FrontUtils.frontPageData(request, model);
		/** 将request中所有参数保存至model中 */
		Map<String, Object> params = RequestUtils.getQueryParams(request);
		model.putAll(params);
		String tpl = FrontUtils.getTplAbsolutePath(request, page, RequestUtils.COMMON_PATH_SEPARATE);
		String view = FrontUtils.getTplPath(request, tpl);
		String viewPath = realPathResolver.get(view);
		boolean tplExist = false;
		if (WebConstants.FREEMARKER_RES_TYPE.equals(freemarkResType)) {
			viewPath = templateLoaderPath + view;
			tplExist = new UrlResource(viewPath).exists();
		} else {
			viewPath = java.text.Normalizer.normalize(viewPath, java.text.Normalizer.Form.NFKD);
			File tplFile = new File(viewPath);
			tplExist = tplFile.exists();
		}
		if (tplExist) {
			return view;
		} else {
			return FrontUtils.pageNotFound(request, response, model);
		}
	}

	/**
	 * 获取公用(全局)数据
	 */
	@GetMapping(value = "/globalInfo")
	@ResponseBody
	@MoreSerializeField({
			@SerializeField(clazz = Area.class, includes = { "id", "areaCode", "areaName", "areaDictCode" }),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "url", "resourceType" }) })
	public ResponseInfo globalInfo(HttpServletRequest request) throws GlobalException {
		GlobalConfigDTO configDTO = configService.filterConfig(request, SystemContextUtils.getGlobalConfig(request));
		return new ResponseInfo(configDTO);
	}

	/**
	 * 获取公用(站点)数据
	 */
	@GetMapping(value = "/systemInfo")
	@ResponseBody
	@MoreSerializeField()
	public ResponseInfo siteInfo(HttpServletRequest request) throws GlobalException {
		GlobalConfig config = SystemContextUtils.getGlobalConfig(request);
		JSONObject object = new JSONObject();
		object.put("memberRegisterOpen", config.getMemberRegisterOpen());
		object.put("MemberRegisterCaptcha", config.getMemberRegisterCaptcha());
		object.put("memberOpen", config.getMemberOpen());
		return new ResponseInfo(object);
	}

	/**
	 * 进入站点关闭提示页面
	 * @Title: toSiteClose
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param model ModelMap
	 * @return: String
	 */
	@RequestMapping(value = WebConstants.SITE_CLOSE, method = RequestMethod.GET)
	public String toSiteClose(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		FrontUtils.frontData(request, model);
		return FrontUtils.showSiteClosePage(request, response, model);
	}

	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private GlobalConfigService configService;
	@Value(value = "${freemarker.resources.type}")
	private String freemarkResType;
	@Value(value = "${freemarker.templateLoaderPath}")
	private String templateLoaderPath;
}

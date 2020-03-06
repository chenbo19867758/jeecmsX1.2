package com.jeecms.common.web.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.util.PropertiesLoader;

/**
 * HttpServletResponse帮助类
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:44:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public final class ResponseUtils {
	public static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

	private static final Map<String, String> HEADMAP;

	static {
		HEADMAP = new HashMap<String, String>();
		HEADMAP.put("jpg", "image/jpeg");
		HEADMAP.put("bmp", "image/bmp");
		HEADMAP.put("jpeg", "image/jpeg");
		HEADMAP.put("png", "image/png");
		HEADMAP.put("gif", "image/gif");
		HEADMAP.put("mp3", "audio/mpeg");
		HEADMAP.put("wma", "audio/x-ms-wma");
		HEADMAP.put("wav", "audio/x-wav");
		HEADMAP.put("amr", "audio/amr");
		HEADMAP.put("mp4", "video/mpeg4");
	}

	/**
	 * 导出excel
	 * 
	 * @Title: exportExcel
	 * @param: @param
	 *             request
	 * @param: @param
	 *             response
	 * @param: @param
	 *             fileName 导出文件名称，不带后缀
	 * @param: @param
	 *             workBook
	 * @param: @throws
	 *             IOException
	 * @return: void
	 */
	public static void exportExcel(HttpServletRequest request, HttpServletResponse response, String fileName,
			Workbook workBook) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		workBook.write(response.getOutputStream());
	}

	/**
	 * 下载素材
	 * 
	 * @Title: downloadMaterial
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param fileName
	 *            文件名
	 * @param suffix
	 *            文件格式
	 * @param bytes
	 *            文件字节数组
	 * @throws IOException
	 *             IOException
	 * @throws InterruptedException
	 *             InterruptedException
	 * @return: void
	 */
	public static void downloadMaterial(HttpServletRequest request, HttpServletResponse response, String fileName,
			String suffix, byte[] bytes) throws IOException, InterruptedException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-Type", HEADMAP.get(suffix));
		fileName = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFKD);
		suffix = java.text.Normalizer.normalize(suffix, java.text.Normalizer.Form.NFKD);
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + "." + suffix, "UTF-8"));
		response.getOutputStream().write(bytes);
	}

	public static void downloadVideo(HttpServletRequest request, HttpServletResponse response, String fileName,
			String suffix, byte[] bytes, String url) throws IOException, InterruptedException {
		if (bytes != null) {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Type", HEADMAP.get(suffix));
			fileName = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFKD);
			suffix = java.text.Normalizer.normalize(suffix, java.text.Normalizer.Form.NFKD);
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode(fileName + "." + suffix, "UTF-8"));
			response.getOutputStream().write(bytes);
		} else {
			JSONObject object = new JSONObject();
			object.put("url", url);
			ResponseUtils.renderJson(response, object.toJSONString());
		}
	}

	/**
	 * 发送文本。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * 发送html。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderHtml(HttpServletResponse response, String text) {
		render(response, "text/html;charset=UTF-8", text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param contentType
	 *            类型
	 * @param text
	 *            字符串
	 */
	public static void render(HttpServletResponse response, String contentType, String text) {
		response.setContentType(contentType);
		setHeader(response);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 设置通用header头
	 * 
	 * @Title: setHeader
	 * @param response
	 * @return: void
	 */
	public static void setHeader(HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,HEAD,OPTIONS,PATCH,DELETE,PUT");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", SysConstants.DEFAULT_ALLOW_HEADERS + getTokenHeader() + ","
				+ getRedirectHeader() + "," + getSiteIdHeader());
		response.setHeader("access-control-allow-credentials", "false");
		response.setHeader("X-Content-Type-Options", "nosniff");
	}

	public static void addHeader(HttpServletResponse response, String headKey, String headVal) {
		response.setHeader(headKey, headVal);
	}

	/**
	 * 重定向到登录页
	 * 
	 * @Title: redirectToLogin
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return: void
	 * @throws IOException
	 *             IOException
	 */
	public static void redirectToLogin(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		String loginUrl = WebConstants.LOGIN_URL;
		String location = RequestUtils.getLocation(request);
		/** 非本站地址 定位到首页，防止 Open Redirect ，但fortify依然还是会报 */
		if (StringUtils.isNoneBlank(location)) {
			if (!location.startsWith(RequestUtils.getServerUrl(request))) {
				location = "/";
			}
		}
		if (StringUtils.isNoneBlank(request.getContextPath())) {
			loginUrl = request.getContextPath() + loginUrl;
		}
		response.sendRedirect(String.format("%s?backUrl=%s", loginUrl, location));
	}
	
	/**
	 * 如果开启单点登录,
	 * 重定向到SSO登录页
	 * 
	 * @Title: redirectToLogin
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return: void
	 * @throws IOException
	 *             IOException
	 */
	public static void redirectToSSOLogin(HttpServletRequest request,
			HttpServletResponse response, String ssoLoginUrl) 
			throws IOException {
		response.sendRedirect(ssoLoginUrl);
	}

	/**
	 * 重定向到站点关闭页面
	 * @Title: redirectToSiteClose
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws IOException IOException
	 * @return: void
	 */
	public static void redirectToSiteClose(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String siteCloseUrl = WebConstants.SITE_CLOSE;
		if (StringUtils.isNoneBlank(request.getContextPath())) {
			siteCloseUrl = request.getContextPath() + siteCloseUrl;
		}
		response.sendRedirect(siteCloseUrl);
	}

	/**
	 * 重定向URL
	 * 
	 * @Title: redirectToUrl
	 * @param url
	 *            url
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 *             IOException
	 */
	public static void redirectToUrl(String url, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		/** 非本站地址 定位到首页，防止 Open Redirect ，但fortify依然还是会报 */
		if (StringUtils.isNoneBlank(url)) {
			if (!url.startsWith(RequestUtils.getServerUrl(request))) {
				url = "/";
			}
		}
		response.sendRedirect(url);
	}

	private static String getTokenHeader() {
		if (StringUtils.isBlank(tokenHeader)) {
			PropertiesLoader loader = new PropertiesLoader();
			loader.setFileEncoding("UTF-8");
			try {
				loader.setValue("classpath:application.properties");
				Properties properties = loader.createProperties();
				tokenHeader = (String) properties.get("token.header");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tokenHeader;
	}

	private static String getRedirectHeader() {
		if (StringUtils.isBlank(redirectHeader)) {
			PropertiesLoader loader = new PropertiesLoader();
			loader.setFileEncoding("UTF-8");
			try {
				loader.setValue("classpath:application.properties");
				Properties properties = loader.createProperties();
				redirectHeader = (String) properties.get("redirect.header");
				siteIdHeader = (String) properties.get("siteId.header");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return redirectHeader;
	}

	private static String getSiteIdHeader() {
		if (StringUtils.isBlank(redirectHeader)) {
			PropertiesLoader loader = new PropertiesLoader();
			loader.setFileEncoding("UTF-8");
			try {
				loader.setValue("classpath:application.properties");
				Properties properties = loader.createProperties();
				siteIdHeader = (String) properties.get("siteId.header");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return siteIdHeader;
	}
	
	public static void renderMp3(HttpServletResponse response, String text) {
		render(response, "audio/mp3;charset=UTF-8", text);
	}

	private static String tokenHeader;

	private static String redirectHeader;

	private static String siteIdHeader;

}

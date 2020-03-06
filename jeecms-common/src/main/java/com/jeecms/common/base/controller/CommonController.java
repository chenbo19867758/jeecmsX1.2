package com.jeecms.common.base.controller;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Producer;
import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.common.captcha.KaptchaConfig;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.Base64Utils;
import com.jeecms.common.util.QrCodeUtil;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.common.wechat.util.client.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共controller层，默认访问前缀为 /common
 * 
 * @author: tom
 * @date: 2018年1月24日 下午15:40:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Controller
public class CommonController<T extends AbstractIdDomain<ID>, ID extends Serializable> {
	static Logger logger = LoggerFactory.getLogger(CommonController.class);

	/**
	 * loadingImage 动态加载网络图片
	 * 
	 * @Description: imageUrl参数为网络图片地址
	 * @param: @param
	 *             request
	 * @param: @param
	 *             response
	 * @param: @throws
	 *             Exception
	 * @return: void
	 */
	@RequestMapping(value = "/loadingImage")
	public void loadingImage(HttpServletRequest request, HttpServletResponse response) {
		response.setDateHeader("Expires", 0);
		// 设置浏览器渲染不做本地缓存
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// 设置浏览器渲染不读取浏览器缓存
		response.setHeader("Pragma", "no-cache");
		// 设置浏览器渲染图片类型
		response.setContentType("image/jpeg");
		String imageUrl = request.getParameter("imageUrl");
		ServletOutputStream out;
		try {
			out = response.getOutputStream();
			out.write(HttpUtil.readURLImage(imageUrl));
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 加载图形验证码
	 * 
	 * @Title: getKaptchaImage
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @throws Exception
	 *             Exception
	 * @return: void
	 */
	@GetMapping("/kaptcha")
	public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);
		// 设置浏览器渲染不做本地缓存
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// 设置浏览器渲染不读取浏览器缓存
		response.setHeader("Pragma", "no-cache");
		// 设置浏览器渲染图片类型
		response.setContentType("image/jpeg");
		// 生成验证码内容
		String capText = captchaProducer.createText();
		// 存储验证码信息
		String sessionId = session.getSessionId(request);
		String paramSessionId = RequestUtils.getParam(request, KaptchaConfig.PARAM_SESSION_ID);
		if (StringUtils.isNoneBlank(paramSessionId)) {
			sessionId = paramSessionId;
		}
		cacheProvider.setCache(CacheConstants.CAPTCHA, WebConstants.KCAPTCHA_PREFIX + sessionId, capText);
		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);
		ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
		ImageIO.write(bi, "jpg",outputStream);
		// write the data out
		//ServletOutputStream out = response.getOutputStream();
		//ImageIO.write(bi, "jpg", out);
		Map<String, Object> map = new HashMap<String, Object>(2);
		
		map.put("img", Base64Utils.imageToBase64ByByte(outputStream.toByteArray()));
		map.put(KaptchaConfig.PARAM_SESSION_ID, sessionId);

		ResponseInfo info = new ResponseInfo(map);
		try {
			outputStream.flush();
		} finally {
			outputStream.close();
		}
		ResponseUtils.renderJson(response, JSON.toJSONString(info));
	}

	/**
	 * 加载二维码图片
	 * 
	 * @Title: getQrcodeImage
	 * @param size
	 *            大小
	 * @param str
	 *            二维码内容
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @throws Exception
	 *             Exception
	 * @return: void
	 */
	@GetMapping("/qrcode/{size}")
	public void getQrcodeImage(@PathVariable("size") int size, @RequestParam("val") String str,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);
		// 设置浏览器渲染不做本地缓存
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// 设置浏览器渲染不读取浏览器缓存
		response.setHeader("Pragma", "no-cache");
		// 设置浏览器渲染图片类型
		response.setContentType("image/jpeg");
		//str = DesUtil.decrypt(str.trim(), ContentSecurityConstants.DES_KEY, ContentSecurityConstants.DES_IV);
		// 创建二维码
		BufferedImage bi = QrCodeUtil.createQrCode(str, size);
		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpeg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}

	@Autowired
	private Producer captchaProducer;
	@Autowired
	private SessionProvider session;
	@Autowired
	private CacheProvider cacheProvider;

}

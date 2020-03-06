package com.jeecms.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.constants.ServerModeEnum;
import com.jeecms.common.util.PropertiesUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Freemarker异常处理
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:32:38
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FreemarkerExceptionHandler implements TemplateExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(FreemarkerExceptionHandler.class);

	@Override
	public void handleTemplateException(TemplateException te, Environment env, Writer out) 
			throws TemplateException {
		if (StringUtils.isBlank(serverMode)) {
			try {
				serverMode = PropertiesUtil.loadSystemProperties()
						.getProperty("spring.profiles.active");
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		log.error("[Freemarker Error: " + te.getMessage() + "]");
		String tipMsg = te.getMessage();
		/** 生产模式下屏蔽freemarker异常信息 */
		if (ServerModeEnum.prod.toString().equals(serverMode)) {
			tipMsg = "<span style='color:red'>[freemarker标签异常，请联系网站管理员]</span>";
		}
		try {
			out.write(tipMsg);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private String serverMode = "";

}

package com.jeecms.common.captcha;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 验证码配置
 * 
 * @author: tom
 * @date: 2018年12月24日 下午5:48:44
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
public class KaptchaConfig {
	/** 会话ID */
	public static String PARAM_SESSION_ID = "sessionId";

	/**
	 * 配置DefaultKaptcha
	 * @Title: getKaptchaBean  
	 * @return: DefaultKaptcha
	 */
	@Bean(name = "captchaProducer")
	public DefaultKaptcha getKaptchaBean() {
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");
		properties.setProperty("kaptcha.textproducer.char.string", "ABCDEGHJKLMNRSTUWXY23456789");
		properties.setProperty("kaptcha.textproducer.font.color", "0,84,144");
		properties.setProperty("kaptcha.noise.color", "0,84,144");
		properties.setProperty("kaptcha.textproducer.font.size", "30");
		properties.setProperty("kaptcha.background.clear.from", "247,255,234");
		properties.setProperty("kaptcha.background.clear.to", "247,255,234");
		properties.setProperty("kaptcha.image.width", "125");
		properties.setProperty("kaptcha.image.height", "35");
		properties.setProperty("kaptcha.session.key", "code");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.names", "Arial, Courier");
		Config config = new Config(properties);
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}
}

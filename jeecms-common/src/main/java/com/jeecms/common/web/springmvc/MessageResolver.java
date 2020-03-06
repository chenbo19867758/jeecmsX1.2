package com.jeecms.common.web.springmvc;

import java.util.Locale;
import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Message Resolver
 * @author: tom
 * @date: 2018年12月17日 下午2:49:38
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class MessageResolver {
	private static MessageSource ms;

	public static MessageSource getMs() {
		return ms;
	}

	public static void setMs(MessageSource ms) {
		MessageResolver.ms = ms;
	}

	@Resource
	public void setMessageSource(MessageSource messageSource) {
		setMs(messageSource);
	}

	public static MessageSource getMessageSource() {
		return getMs();
	}

	public static Locale getCurrentLocale() {
		return LocaleContextHolder.getLocale();
	}

	public static String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
		return ms.getMessage(resolvable, getCurrentLocale());
	}

	public static String getMessage(String code, Object[] args) throws NoSuchMessageException {
		return ms.getMessage(code, args, getCurrentLocale());
	}

	public static String getMessage(String code) throws NoSuchMessageException {
		return ms.getMessage(code, null, getCurrentLocale());
	}

	public static String getMessage(String code, Object[] args, String defaultMsg) {
		return ms.getMessage(code, args, defaultMsg, getCurrentLocale());
	}

	public static String getMessage(String code, String defaultMsg) {
		return getMessage(code, null, defaultMsg);
	}
}

package com.jeecms.common.web.springmvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.jeecms.common.web.ApplicationContextProvider;

import freemarker.core.ParseException;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;

/**
 * 轻量级的FreeemarkerView 不支持jsp标签、不支持request、session、application等对象，可用于前台模板页面。
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:41:33
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SimpleFreeMarkerView extends AbstractTemplateView {
	/**
	 * 部署路径调用名称
	 */
	public static final String CONTEXT_PATH = "base";

	private Configuration configuration;

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	protected Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * 自动检测FreeMarkerConfig
	 * 
	 * @Title: autodetectConfiguration
	 * @throws ApplicationContextException  ApplicationContextException
	 * @return: FreeMarkerConfig
	 */
	protected FreeMarkerConfig autodetectConfiguration() throws ApplicationContextException {
		ApplicationContext appCtx = getApplicationContext();
		if (appCtx == null) {
			appCtx = ApplicationContextProvider.getApplicationContext();
		}
		return (FreeMarkerConfig) BeanFactoryUtils.beanOfTypeIncludingAncestors(
				appCtx, FreeMarkerConfig.class, true,false);
	}

	/**
	 * Invoked on startup. Looks for a single FreeMarkerConfig bean to find the
	 * relevant Configuration for this factory.
	 * Checks that the template for the default Locale can be found: FreeMarker
	 * will check non-Locale-specific templates if a locale-specific one is not
	 * found.
	 * 
	 * @see freemarker.cache.TemplateCache#getTemplate
	 */
	@Override
	protected void initApplicationContext() throws ApplicationContextException {
		super.initApplicationContext();
		if (getConfiguration() == null) {
			FreeMarkerConfig config = autodetectConfiguration();
			Configuration configuration = config.getConfiguration();
			configuration.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
			setConfiguration(configuration);
		}
		// checkTemplate();
	}

	/**
	 * Check that the FreeMarker template used for this view exists and is
	 * valid.
	 * Can be overridden to customize the behavior, for example in case of
	 * multiple templates to be rendered into a single view.
	 * 
	 * @throws ApplicationContextException
	 *             if the template cannot be found or is invalid
	 */
	protected void checkTemplate() throws ApplicationContextException {
		try {
			// Check that we can get the template, even if we might subsequently
			// get it again.
			getConfiguration().getTemplate(getUrl());
		} catch (ParseException ex) {
			throw new ApplicationContextException("Failed to parse FreeMarker template for URL [" 
					+ getUrl() + "]", ex);
		} catch (IOException ex) {
			throw new ApplicationContextException("Could not load FreeMarker template for URL [" 
					+ getUrl() + "]", ex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void renderMergedTemplateModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		model.put(CONTEXT_PATH, request.getContextPath());
		getConfiguration().getTemplate(getUrl()).process(model, response.getWriter());
	}
}

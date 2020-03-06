/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.component.listener.AbstractContentListener;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.GlobalConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 百度新闻推送服务
 *
 * @author tom
 * @version 1.0
 * @date 2019年5月11日 下午6:40:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentPushServiceImpl extends AbstractContentListener {

	private static final Logger log = LoggerFactory.getLogger(ContentPushServiceImpl.class);

	/**
	 * 百度主动推送链接提交地址key
	 */
	@Value("${baidu.linksubmit.address}")
	private String linkKey;
	/**
	 * 百度主动推送链接提交Host
	 */
	@Value("${baidu.linksubmit.host}")
	private String linkHost;

	@Override
	public void afterDelete(List<Content> contents) throws GlobalException {
		super.afterDelete(contents);
	}

	@Override
	public void afterSave(Content content) {
		if (content.getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
			String result = pushPost(content);
		}
	}


	@Override
	public Map<String, Object> preChange(Content content) {
		return super.preChange(content);
	}

	@Override
	public void afterChange(Content content, Map<String, Object> map) throws GlobalException {
		if (content.getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
			String result = pushPost(content);
		}
	}

	/**
	 * 百度链接实时推送
	 *
	 * @param content 内容
	 * @return
	 */
	private String pushPost(Content content) {
		CmsSite site = siteService.findById(content.getSiteId());
		String domain = site.getDomain();
		GlobalConfig globalConfig = null;
		try {
			globalConfig = configService.getGlobalConfig();
		} catch (GlobalException e) {
			log.error("配置信息获取失败!{}", e.getMessage());
		}
		Boolean isBaiduPush = globalConfig.getConfigAttr().getIsBaiduPush();
		String token = globalConfig.getConfigAttr().getBaiduPushToken();
		String result ="";
		if(isBaiduPush&& StringUtils.isNotBlank(token)){
			//if (!isBaiduPush) {
			//return "推送未开启";
			//}
			linkKey += "?site=" + domain + "&token=" + token;
			//HttpClient
			CloseableHttpClient client;
			client = (CloseableHttpClient) wrapClient();
			Map<String, String> msg = new HashMap<>();
			HttpPost post = new HttpPost(linkKey);
			//发送请求参数
			try {
				StringEntity s = new StringEntity(content.getUrl(), "utf-8");
				s.setContentType("application/json");
				post.setEntity(s);
				post.setHeader("Host", linkHost);
				post.setHeader("User-Agent", "curl/7.12.1");
				post.setHeader("Content-Type", "text/plain");
				HttpResponse res = client.execute(post);
				HttpEntity entity = res.getEntity();
				result = EntityUtils.toString(entity, "utf-8");
				log.info("baidu link submit result -> " + result);
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}

		}

		return result;
	}

	private static HttpClient wrapClient() {
		try {
			// 在调用SSL之前需要重写验证方法，取消检测SSL
			X509TrustManager trustManager = new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] xcs, String str) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] xcs, String str) {
				}
			};
			SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
			ctx.init(null, new TrustManager[]{trustManager}, null);
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
			// 创建Registry
			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
					.setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
					.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", socketFactory).build();
			// 创建ConnectionManager，添加Connection配置信息
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			return HttpClients.custom().setConnectionManager(connectionManager)
					.setDefaultRequestConfig(requestConfig).build();
		} catch (KeyManagementException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Autowired
	private GlobalConfigService configService;
	@Autowired
	private CmsSiteService siteService;
}

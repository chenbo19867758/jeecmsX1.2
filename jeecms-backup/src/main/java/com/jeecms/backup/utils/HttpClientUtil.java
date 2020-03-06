package com.jeecms.backup.utils;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient 工具类
 *
 * @author: zhu kaixiao
 * @date: 2019年8月2日 上午17:22:18
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class HttpClientUtil {
	static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 以application/json的形式提交post请求 对应spring mvc中的@RequestBody注解
	 *
	 * @param url
	 *            url
	 * @param params
	 *            参数
	 * @return java.lang.String
	 * @author Zhu Kaixiao
	 * @date 2019/8/2 17:23
	 **/
	public static <P> String postJson(String url, P params) {
		String result = "";
		long postTime = System.currentTimeMillis();
		try {
			HttpPost post = new HttpPost(url);
			Header applicationJson = new BasicHeader("Content-Type", "application/json;charset=UTF-8");
			post.addHeader(applicationJson);
			log.debug("POST-JSON{{}}:     URL: {}", postTime, url);
			if (params != null) {
				String jsonString = JSONObject.toJSONString(params);
				StringEntity entity = new StringEntity(jsonString, "utf-8");
				entity.setContentEncoding(applicationJson);
				post.setEntity(entity);
				log.debug("POST-JSON{{}}:  PARAMS: {}", postTime, jsonString);
			}

			// 发送请求并接收返回数据
			CloseableHttpClient httpClient = null;
			CloseableHttpResponse response = null;
			try {
				httpClient = HttpClients.createDefault();
				response = httpClient.execute(post);
				result = EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			}
			log.debug("POST-JSON{{}}:  RESULT: {}", postTime, result);
		} catch (Exception e) {
			log.error("POST-JSON{{}}:  ERROR: {}", postTime, e.getMessage());
		}
		return result;
	}

}

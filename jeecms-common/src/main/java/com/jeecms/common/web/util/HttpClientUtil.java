package com.jeecms.common.web.util;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ConnectTimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.SocketTimeoutException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HttpClient 工具类
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:44:19
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class HttpClientUtil {
	static final int RESPONSE_ERROR_CODE = 300;
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static class SingletonHolder {
		private static final HttpClientUtil INSTANCE = new HttpClientUtil();
	}

	private HttpClientUtil() {
	}

	public static HttpClientUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Get方式抓取url的内容
	 * 
	 * @Title: get
	 * @param url
	 *            url
	 * @return: String
	 */
	public static String get(String url) {
		CharsetHandler handler = new CharsetHandler("UTF-8");
		CloseableHttpClient client = null;
		try {
			HttpGet httpget = new HttpGet(new URI(url));
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			client = httpClientBuilder.build();
			client = (CloseableHttpClient) wrapClient(client);
			return client.execute(httpget, handler);
		} catch (Exception e) {
			// e.printStackTrace();
			return "";
		} finally {
			try {
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * post方式提交到url
	 * 
	 * @Title: postParams
	 * @param url
	 *            url
	 * @param params
	 *            请求参数map
	 * @return: String
	 */
	public static String postParams(String url, Map<String, String> params) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient client = httpClientBuilder.build();
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse res = null;
		try {

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, params.get(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			res = client.execute(post);
			HttpEntity entity = res.getEntity();
			return EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (res != null) {
					res.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			try {
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

		}
		return "";
	}




	/**
	 * 以application/json的形式提交post请求
	 * 对应spring mvc中的@RequestBody注解
	 *
	 * @param url    url
	 * @param params 参数
	 * @return java.lang.String
	 * @author Zhu Kaixiao
	 * @date 2019/8/2 17:23
	 * @throws IOException 网络连接失败时抛出IOException
	 **/
	public static <P> String postJson(String url, P params,Map<String,String>headers) throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		Header applicationJson = new BasicHeader("Content-Type", "application/json;charset=UTF-8");
		post.addHeader(applicationJson);
		if(headers!=null){
			Set<Map.Entry<String,String>>headEntries = headers.entrySet();
			for(Map.Entry<String,String> en:headEntries){
				post.addHeader(new BasicHeader(en.getKey(), en.getValue()));
			}
		}
		if (params != null) {
			StringEntity entity = new StringEntity(JSONObject.toJSONString(params), "utf-8");
			entity.setContentEncoding(applicationJson);
			post.setEntity(entity);
		}

		//发送请求并接收返回数据
		CloseableHttpResponse response = httpClient.execute(post);
		String result = EntityUtils.toString(response.getEntity());
		response.close();
		httpClient.close();
		return result;
	}
	
	public static <P> String timeLimitPostJson(String url, P params, Map<String, String> headers, Integer time)
			throws ParseException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		// 设置10秒超时
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(time).setSocketTimeout(time)
				.setConnectTimeout(time).build();
		// 将超时策略放入其中
		post.setConfig(requestConfig);
		Header applicationJson = new BasicHeader("Content-Type", "application/json;charset=UTF-8");
		post.addHeader(applicationJson);
		if (headers != null) {
			Set<Map.Entry<String, String>> headEntries = headers.entrySet();
			for (Map.Entry<String, String> en : headEntries) {
				post.addHeader(new BasicHeader(en.getKey(), en.getValue()));
			}
		}
		if (params != null) {
			StringEntity entity = new StringEntity(JSONObject.toJSONString(params), "utf-8");
			entity.setContentEncoding(applicationJson);
			post.setEntity(entity);
		}
		// 发送请求并接收返回数据
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		// 如果超时会进入到该catch中，如果进入catch中则说明是超时了，超时则直接过滤
		} catch (SocketTimeoutException | ConnectTimeoutException ex) {
			// 请求超时
			return null;
		} finally {
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		}
	}

	public static <P> String postJson(String url, P params) throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		Header applicationJson = new BasicHeader("Content-Type", "application/json;charset=UTF-8");
		post.addHeader(applicationJson);

		if (params != null) {
			StringEntity entity = new StringEntity(JSONObject.toJSONString(params), "utf-8");
			entity.setContentEncoding(applicationJson);
			post.setEntity(entity);
		}
		//发送请求并接收返回数据
		CloseableHttpResponse response = httpClient.execute(post);
		String result = EntityUtils.toString(response.getEntity());
		response.close();
		httpClient.close();
		return result;
	}

	/**
	 * 以application/json的形式提交post请求
	 * 对应spring mvc中的@RequestBody注解
	 *
	 * @param url    url
	 * @param params 参数
	 * @return java.lang.String
	 * @author Zhu Kaixiao
	 * @date 2019/8/2 17:23
	 * @throws IOException 网络连接失败时抛出IOException
	 **/
	public static <P> String sendByDeleteMethod(String url, P params,Map<String,String>headers) throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		//HttpDelete del = new HttpDelete(url);
		HttpDeleteWithBody del = new HttpDeleteWithBody(url);
		Header applicationJson = new BasicHeader("Content-Type", "application/json;charset=UTF-8");
		del.addHeader(applicationJson);
		if(headers!=null){
			Set<Map.Entry<String,String>>headEntries = headers.entrySet();
			for(Map.Entry<String,String> en:headEntries){
				del.addHeader(new BasicHeader(en.getKey(), en.getValue()));
			}
		}
		if (params != null) {
			StringEntity entity = new StringEntity(JSONObject.toJSONString(params), "utf-8");
			entity.setContentEncoding(applicationJson);
			del.setEntity(entity);
		}

		//发送请求并接收返回数据
		CloseableHttpResponse response = httpClient.execute(del);
		String result = EntityUtils.toString(response.getEntity());
		response.close();
		httpClient.close();
		return result;
	}

	public static <P> String get(String url, Map<String,String>headers) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		Header applicationJson = new BasicHeader("Content-Type", "application/json;charset=UTF-8");
		get.addHeader(applicationJson);
		if(headers!=null){
			Set<Map.Entry<String,String>>headEntries = headers.entrySet();
			for(Map.Entry<String,String> en:headEntries){
				get.addHeader(new BasicHeader(en.getKey(), en.getValue()));
			}
		}
		//发送请求并接收返回数据
		CloseableHttpResponse response = httpClient.execute(get);
		String result = EntityUtils.toString(response.getEntity());
		response.close();
		httpClient.close();
		return result;
	}



	/**
	 * post方式提交到url
	 * 
	 * @Title: post
	 * @param url
	 *            url
	 * @param params
	 *            请求参数map
	 * @param contentType
	 *            请求类型
	 * @return: String
	 */
	public static String post(String url, String params, String contentType) {

		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient client = httpClientBuilder.build();
		client = (CloseableHttpClient) wrapClient(client);

		HttpPost post = new HttpPost(url);
		CloseableHttpResponse res = null;
		try {
			StringEntity s = new StringEntity(params, "UTF-8");
			if (StringUtils.isBlank(contentType)) {
				s.setContentType("application/json");
			}
			s.setContentType(contentType);
			s.setContentEncoding("utf-8");
			post.setEntity(s);
			res = client.execute(post);
			HttpEntity entity = res.getEntity();
			return EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (res != null) {
					res.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return "";
	}

	/**
	 * 发送xml格式数据到url
	 * 
	 * @Title: post
	 * @param urlStr
	 *            url
	 * @param xmlInfo
	 *            xml数据
	 * @return: String
	 */
	public static String post(String urlStr, String xmlInfo) {
		String line1 = "";
		OutputStreamWriter out = null;
		BufferedReader br = null;
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			// con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			out = new OutputStreamWriter(outputStream = con.getOutputStream());
			out.write(new String(xmlInfo.getBytes("utf-8")));
			out.flush();
			out.close();
			br = new BufferedReader(inputStreamReader = new InputStreamReader(
					inputStream = con.getInputStream()));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				line1 += line;
			}
			return new String(line1.getBytes(), "utf-8");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static class CharsetHandler implements ResponseHandler<String> {
		private String charset;

		public CharsetHandler(String charset) {
			this.charset = charset;
		}

		@Override
		public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= RESPONSE_ERROR_CODE) {
				throw new HttpResponseException(statusLine.getStatusCode(), 
						statusLine.getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (!StringUtils.isBlank(charset)) {
					return EntityUtils.toString(entity, charset);
				} else {
					return EntityUtils.toString(entity);
				}
			} else {
				return null;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static HttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLSv1");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] xcs, String string) 
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] xcs, String string) 
						throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx, 
					new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf)
					.build();
			return httpclient;

		} catch (Exception ex) {
			return null;
		}
	}

}

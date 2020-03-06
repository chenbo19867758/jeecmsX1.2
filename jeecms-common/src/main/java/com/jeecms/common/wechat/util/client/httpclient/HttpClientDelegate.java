package com.jeecms.common.wechat.util.client.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.util.client.HttpDelegate;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * @Description:httpclient实现类(适用于公众平台)
 * @author: qqwang
 * @date: 2018年6月21日 下午3:31:55
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class HttpClientDelegate extends AbstractHttpClient implements HttpDelegate {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientDelegate.class);
	private static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.custom().build(false, null, null);

	@Override
	public String get(String url, Map<String, String> params) {
		String response = null;
		try {
			response = HTTP_CLIENT.execute(buildGetRequest(url, params), STRING_HANDLER);
			LOGGER.info(java.text.Normalizer.normalize(String.format("get request url:%s, result:%s", url, response),
					java.text.Normalizer.Form.NFKD));
		} catch (Exception e) {
			LOGGER.error(java.text.Normalizer.normalize(String.format("Get error.url: %s, params: %s", url, params),
					java.text.Normalizer.Form.NFKD), e);
		}
		return response;
	}

	@Override
	public String post(String url, Map<String, String> params, String postData) {
		String response = null;
		try {
			response = HTTP_CLIENT.execute(buildPostRequest(url, params, postData), STRING_HANDLER);
			LOGGER.info(java.text.Normalizer.normalize(String.format("Post request url:%s, result:%s", url, response),
					java.text.Normalizer.Form.NFKD));
		} catch (Exception e) {
			LOGGER.error(java.text.Normalizer.normalize(
					String.format("Post error.url: %s , params: %s , postData: %s", url, params, postData),
					java.text.Normalizer.Form.NFKD), e);
		}
		return response;
	}

	@Override
	public String post(String url, String mchId, String certPath, Map<String, String> params, String postData) {
		String response = null;
		CloseableHttpClient certHttpClient = null;
		try {
			certHttpClient = HttpClientBuilder.custom().build(true, mchId, certPath);
			response = certHttpClient.execute(buildPostRequest(url, params, postData), STRING_HANDLER);
			LOGGER.info(java.text.Normalizer.normalize(String.format("Post request url:%s, result:%s", url, response),
					java.text.Normalizer.Form.NFKD));
		} catch (Exception e) {
			LOGGER.error(java.text.Normalizer.normalize(
					String.format("Post error.url: %s, params: %s, postData: %s", url, params, postData),
					java.text.Normalizer.Form.NFKD), e);
		} finally {
			if (certHttpClient != null) {
				try {
					certHttpClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	@Override
	public String upload(String url, Map<String, String> params, String data, File file) {
		String response = null;
		try {
			response = HTTP_CLIENT.execute(buildUploadRequest(url, params, data, file), STRING_HANDLER);
			LOGGER.info(java.text.Normalizer.normalize(String.format("Upload request url:%s, result:%s", url, response),
					java.text.Normalizer.Form.NFKD));
		} catch (Exception e) {
			LOGGER.error(java.text.Normalizer.normalize(String.format("Upload error.url: %s, params: %s", url, params),
					java.text.Normalizer.Form.NFKD), e);
		}
		return response;
	}

	@Override
	public MediaFile downloadByGet(String url, Map<String, String> params) {
		MediaFile response = null;
		try {
			response = HTTP_CLIENT.execute(buildGetRequest(url, params), FILE_HANDLER);
			LOGGER.info(java.text.Normalizer.normalize(
					String.format("Download request url:%s, result:%s", url, response.toString()),
					java.text.Normalizer.Form.NFKD));
		} catch (Exception e) {
			LOGGER.error(
					java.text.Normalizer.normalize(String.format("Download error.url: %s, params: %s", url, params),
							java.text.Normalizer.Form.NFKD),
					e);
		}
		return response;
	}

	@Override
	public MediaFile downloadByPost(String url, Map<String, String> params, String data) {
		MediaFile response = null;
		try {
			response = HTTP_CLIENT.execute(buildPostRequest(url, params, data), FILE_HANDLER);
			LOGGER.info(java.text.Normalizer.normalize(
					String.format("Download request url:%s, result:%s", url, response.toString()),
					java.text.Normalizer.Form.NFKD));
		} catch (Exception e) {
			LOGGER.error(
					java.text.Normalizer.normalize(String.format("Download error.url: %s, params: %s", url, params),
							java.text.Normalizer.Form.NFKD),
					e);
		}
		return response;
	}

	@Override
	public <T> T getJsonBean(String url, Map<String, String> params, Class<T> clazz) {
		return SerializeUtil.jsonToBean(get(url, params), clazz);
	}

	@Override
	public <T> T postJsonBean(String url, Map<String, String> params, String postData, Class<T> clazz) {
		return SerializeUtil.jsonToBean(post(url, params, postData), clazz);
	}

	@Override
	public <T> T uploadJsonBean(String url, Map<String, String> params, String postData, File file, Class<T> clazz) {
		return SerializeUtil.jsonToBean(upload(url, params, postData, file), clazz);
	}

	@Override
	public <T> T getXmlBean(String url, Map<String, String> params, Class<T> clazz) {
		return SerializeUtil.xmlToBean(get(url, params), clazz);
	}

	@Override
	public <T> T postXmlBean(String url, Map<String, String> params, String postData, Class<T> clazz) {
		return SerializeUtil.xmlToBean(post(url, params, postData), clazz);
	}

	@Override
	public byte[] readInputStream(InputStream inStream) throws IOException {
		byte[] result = null;
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {
			// 创建一个Buffer字符串
			byte[] buffer = new byte[1024];
			// 每次读取的字符串长度，如果为-1，代表全部读取完毕
			int len = 0;
			// 使用一个输入流从buffer里把数据读取出来
			while ((len = inStream.read(buffer)) != -1) {
				// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
			// 关闭输入流
			inStream.close();
			// 把outStream里的数据写入内存
			result = outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public byte[] readURLImage(String imageUrl) throws IOException {
		if (StringUtils.isBlank(imageUrl)) {
			return null;
		}
		byte[] result = null;
		URL url = new URL(imageUrl);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"GET"
		conn.setRequestMethod("GET");
		// 超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		try (InputStream inStream = conn.getInputStream();) {
			// 通过输入流获取图片数据
			result = readInputStream(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public byte[] readURLVideo(String videoUrl) throws IOException {
		if (StringUtils.isBlank(videoUrl)) {
			return null;
		}
		byte[] result = null;
		URL url = new URL(videoUrl);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"GET"
		conn.setRequestMethod("GET");
		// 超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		try (InputStream inStream = conn.getInputStream();) {
			result = readInputStream(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String postForJson(String url, Map<String, String> params, String postData) {
		String response = null;
		try {
			response = HTTP_CLIENT.execute(buildPostRequestForJson(url, params, postData), STRING_HANDLER);
			LOGGER.info(java.text.Normalizer.normalize(String.format("Post request url:%s, result:%s", url, response),
					java.text.Normalizer.Form.NFKD));
		} catch (Exception e) {
			LOGGER.error(java.text.Normalizer.normalize(
					String.format("Post error.url: %s , params: %s , postData: %s", url, params, postData),
					java.text.Normalizer.Form.NFKD), e);
		}
		return response;
	}

}

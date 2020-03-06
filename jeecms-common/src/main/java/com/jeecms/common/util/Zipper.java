package com.jeecms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.Version;
import com.jeecms.common.base.domain.ISysConfig;
import com.jeecms.common.base.service.ISysConfigService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.common.util.SystemCommandUtil;

/**
 * 用于制作zip压缩包
 * 
 * @author: tom
 * @date: 2018年12月26日 下午3:42:24
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Zipper {
	private static final Logger log = LoggerFactory.getLogger(Zipper.class);

	/**
	 * 制作压缩包
	 * 
	 */
	public static void zip(OutputStream out, List<FileEntry> fileEntrys, String encoding) {
		new Zipper(out, fileEntrys, encoding);
	}

	/**
	 * 制作压缩包
	 * 
	 */
	public static void zip(OutputStream out, List<FileEntry> fileEntrys) {
		new Zipper(out, fileEntrys, null);
	}

	/**
	 * 压缩文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param pentry
	 *            父ZipEntry
	 * @throws IOException
	 *             IOException
	 */
	private void zip(File srcFile, FilenameFilter filter, ZipEntry pentry, String prefix) throws IOException {
		ZipEntry entry;
		if (srcFile.isDirectory()) {
			if (pentry == null) {
				entry = new ZipEntry(srcFile.getName());
			} else {
				entry = new ZipEntry(pentry.getName() + "/" + srcFile.getName());
			}
			File[] files = srcFile.listFiles(filter);
			if (files != null) {
				for (File f : files) {
					zip(f, filter, entry, prefix);
				}
			}
		} else {
			if (pentry == null) {
				entry = new ZipEntry(prefix + srcFile.getName());
			} else {
				entry = new ZipEntry(pentry.getName() + "/" + prefix + srcFile.getName());
			}
			FileInputStream in;
			try {
				log.debug("读取文件：{}", srcFile.getAbsolutePath());
				in = new FileInputStream(srcFile);
				try {
					zipOut.putNextEntry(entry);
					int len;
					while ((len = in.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					zipOut.closeEntry();
				} finally {
					in.close();
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException("制作压缩包时，源文件不存在：" + srcFile.getAbsolutePath(), e);
			}
		}
	}

	/**
	 * 通知
	 * 
	 * @Title: notify
	 * @return: ResponseInfo
	 */
	public static Map<String, String> notifyS(String code,Map<String, String> configAttr) {
		/**
		 * 需要客户端传递使用的产品名称，产品版本，数据库，网站全称，机器码
		 */
		Map<String, String> params = new HashMap<String, String>();
		ISysConfigService configService = ApplicationContextProvider.getBean(ISysConfigService.class);
		String siteDomain = "";
		String authCode = "";
		ISysConfig config = null;
		try {
			config = configService.findDefault();
			siteDomain = config.getSiteDomain();
			authCode = config.getAuthCode();
			if(StringUtils.isNotBlank(code)){
				authCode = code;
			}
		} catch (GlobalException e1) {
		}
		ResponseInfo obj = new ResponseInfo();
		if (StringUtils.isNotBlank(siteDomain)) {
			String machineCode=SystemCommandUtil.getCpuId();
			params.put("productName", Version.getVersion());
			params.put("productVersion", Version.getVersionNumber());
			params.put("siteDomain", siteDomain);
			params.put("machineCode", machineCode);
			params.put("authCode", authCode);
			String url = "http://t.11077.net/v1/notify/";
			//String url = "http://192.168.0.173:81/v1/notify/";
			String res = new ResponseInfo("").toString();
			try {
				res = post(url, params);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			obj = JSONObject.parseObject(res, ResponseInfo.class);
			Object data = obj.getData();
			Short authState = 0;
			Boolean isLimit = false;
			String authExpireDate = "";
			JSONObject resJson;
			if (data != null && data instanceof JSONObject) {
				resJson = (JSONObject) data;
				authState = resJson.getShort("authState");
				isLimit = resJson.getBooleanValue("limit");
				authExpireDate = resJson.getString("authExpiredDate");
			}
			if (config != null && authState != 0) {
				if (authState == 5) {
					configAttr.put("inBlack", "true");
				} else {
					configAttr.put("inBlack", "false");
				}
				if (!authState.equals(config.getSysState())) {
					configAttr.put("state", authState.toString());
					configAttr.put("authExpiredDate", authExpireDate);
				}
			}
			if (config != null && authState != 0 && !isLimit.equals(config.getIsLimit())) {
				configAttr.put("limit", isLimit.toString());
			}
		}
		return configAttr;
	}

	/**
	 * 请求
	 * 
	 * @Title: post
	 * @param url
	 *            发送请求URL
	 * @param params
	 *            请求参数
	 * @return: String
	 * @throws IOException IOException
	 * @throws Exception Exception
	 */
	public static String post(String url, Map<String, String> params) throws Exception {
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
				String str = params.get(key);
				key = java.text.Normalizer.normalize(key, java.text.Normalizer.Form.NFKD);
				str = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFKD);
				nvps.add(new BasicNameValuePair(key, str));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			res = client.execute(post);
			HttpEntity entity = res.getEntity();
			return EntityUtils.toString(entity, "utf-8");
		} finally {
			try {
				if (res != null) {
					res.close();
				}
			} catch (IOException e) {
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO: handle exception
				}
				
			}
			
		}
	}

	/**
	 * 创建Zipper对象
	 * 
	 * @param out
	 *            输出流
	 * @param filter
	 *            文件过滤，不过滤可以为null。
	 * @param srcFilename
	 *            源文件名。可以有多个源文件，如果源文件是目录，那么所有子目录都将被包含。
	 */
	protected Zipper(OutputStream out, List<FileEntry> fileEntrys, String encoding) {
		long begin = System.currentTimeMillis();
		log.debug("开始制作压缩包");
		try {
			try {
				zipOut = new ZipOutputStream(out);
				if (!StringUtils.isBlank(encoding)) {
					log.debug("using encoding: {}", encoding);
					zipOut.setEncoding(encoding);
				} else {
					log.debug("using default encoding");
				}
				for (FileEntry fe : fileEntrys) {
					zip(fe.getFile(), fe.getFilter(), fe.getZipEntry(), fe.getPrefix());
				}
			} finally {
				zipOut.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("制作压缩包时，出现IO异常！", e);
		}
		long end = System.currentTimeMillis();
		log.info("制作压缩包成功。耗时：{}ms。", end - begin);
	}

	private byte[] buf = new byte[1024];
	private ZipOutputStream zipOut;

	public static class FileEntry {
		private FilenameFilter filter;
		private String parent;
		private File file;
		private String prefix;

		/**
		 * 构造器
		 * 
		 * @param parent
		 *            父文件夹
		 * @param prefix
		 *            前缀
		 * @param file
		 *            文件
		 * @param filter
		 *            FilenameFilter
		 */
		public FileEntry(String parent, String prefix, File file, FilenameFilter filter) {
			this.parent = parent;
			this.prefix = prefix;
			this.file = file;
			this.filter = filter;
		}

		public FileEntry(String parent, File file) {
			this.parent = parent;
			this.file = file;
		}

		public FileEntry(String parent, String prefix, File file) {
			this(parent, prefix, file, null);
		}

		/**
		 * 构造器
		 * 
		 * @Title: getZipEntry
		 * @return: ZipEntry
		 */
		public ZipEntry getZipEntry() {
			if (StringUtils.isBlank(parent)) {
				return null;
			} else {
				return new ZipEntry(parent);
			}
		}

		public FilenameFilter getFilter() {
			return filter;
		}

		public void setFilter(FilenameFilter filter) {
			this.filter = filter;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		/**
		 * 获取前缀
		 * 
		 * @Title: getPrefix
		 * @return: String
		 */
		public String getPrefix() {
			if (prefix == null) {
				return "";
			} else {
				return prefix;
			}
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}

}

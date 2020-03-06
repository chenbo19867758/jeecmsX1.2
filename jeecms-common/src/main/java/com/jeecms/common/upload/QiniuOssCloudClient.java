package com.jeecms.common.upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 七牛云上传工具
 * 
 * @author: tom
 * @date: 2018年4月14日 下午3:59:59
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class QiniuOssCloudClient {
	private static final Logger log = LoggerFactory.getLogger(QiniuOssCloudClient.class);
	public static final String RESULT_KEY = "key";
	public static final String RESULT_HASE = "hash";
	public static final String PATH_SPT = "/";
	/**
	 * 存储区域地区简码-华东
	 */
	public static final String BUCKET_AREA_HD = "z0";
	/**
	 * 存储区域地区简码-华北
	 */
	public static final String BUCKET_AREA_HB = "z1";
	/**
	 * 存储区域地区简码-华南
	 */
	public static final String BUCKET_AREA_HN = "z2";
	/**
	 * 存储区域地区简码-北美
	 */
	public static final String BUCKET_AREA_BM = "na0";
	/**
	 * 存储区域地区简码-东南亚
	 */
	public static final String BUCKET_AREA_DNY = "as0";

	/**
	 * 本地文件上传
	 * 
	 * @Title: uploadFileByLocal
	 * @param bucket
	 *            bucket
	 * @param accessKey
	 *            AppKey
	 * @param secretKey
	 *            SecretId
	 * @param ossPath
	 *            上传目标路径
	 * @param localPath
	 *            本地文件（绝对路径）
	 * @return: Map
	 * @throws GlobalException
	 *             GlobalException
	 */
	public static Map<String, String> uploadFileByLocal(String buckArea, String bucket, String accessKey,
			String secretKey, String ossPath, String localPath) throws GlobalException {
		Map<String, String> result = new HashMap<String, String>(2);
		UploadManager uploadManager = getUploadManager(buckArea);
		// 如果是Windows情况下，格式是 D:\\qiniu\\test.png
		String localFilePath = localPath;
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = ossPath;
		String upToken = getAuthToken(bucket, accessKey, secretKey);
		if (StringUtils.isNotBlank(ossPath)) {
			if (ossPath.startsWith(PATH_SPT)) {
				key = ossPath.substring(1);
			}
			try {
				Response response = uploadManager.put(localFilePath, key, upToken);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				result.put(RESULT_KEY, putRet.key);
				result.put(RESULT_HASE, putRet.hash);
			} catch (QiniuException ex) {
				Response r = ex.response;
				log.error(r.toString());
				try {
					log.error(r.bodyString());
				} catch (QiniuException ex2) {
					// ignore
				}
			}
		}
		return result;
	}

	/**
	 * 以字节数据上传
	 * 
	 * @Title: uploadFileByByte
	 * @param bucket
	 *            bucket
	 * @param accessKey
	 *            AppKey
	 * @param secretKey
	 *            SecretId
	 * @param ossPath
	 *            目录路径
	 * @param content
	 *            字节数组
	 * @return: Map
	 * @throws GlobalException
	 *             GlobalException
	 */
	public static Map<String, String> uploadFileByByte(String buckArea, String bucket, String accessKey,
			String secretKey, String ossPath, byte[] content) throws GlobalException {
		Map<String, String> result = new HashMap<String, String>(2);
		UploadManager uploadManager = getUploadManager(buckArea);
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = ossPath;
		String upToken = getAuthToken(bucket, accessKey, secretKey);
		if (StringUtils.isNotBlank(ossPath)) {
			/// u/cms/www/201712/xxxxx.jpg
			/// 截成u/cms/www/201712/xxxxx.jpg,否则返回URL会无法访问到文件
			if (ossPath.startsWith(WebConstants.SPT)) {
				key = ossPath.substring(1);
			}
			try {
				Response response = uploadManager.put(content, key, upToken);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				result.put(RESULT_KEY, putRet.key);
				result.put(RESULT_HASE, putRet.hash);
			} catch (QiniuException ex) {
				Response r = ex.response;
				log.error(r.toString());
				try {
					log.error(r.bodyString());
				} catch (QiniuException ex2) {
					// ignore
				}
			}
		}
		return result;
	}

	/**
	 * 以文件流的形式上传到七牛云OSS
	 * 
	 * @Title: uploadFileByInputStream
	 * @param bucket
	 *            bucket
	 * @param accessKey
	 *            AppKey
	 * @param secretKey
	 *            SecretId
	 * @param ossPath
	 *            目标路径
	 * @param inputStream
	 *            输入流
	 * @return: boolean
	 * @throws GlobalException
	 *             GlobalException
	 */
	public static boolean uploadFileByInputStream(String buckArea, String bucket, 
			String accessKey, String secretKey,
			String ossPath, InputStream inputStream) throws GlobalException {
		Map<String, String> result = new HashMap<String, String>(2);
		UploadManager uploadManager = getUploadManager(buckArea);
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = ossPath;
		String upToken = getAuthToken(bucket, accessKey, secretKey);
		if (StringUtils.isNotBlank(ossPath)) {
			try {
				/// u/cms/www/201712/xxxxx.jpg
				/// 截成u/cms/www/201712/xxxxx.jpg,否则返回URL会无法访问到文件
				if (ossPath.startsWith(WebConstants.SPT)) {
					key = ossPath.substring(1);
				}
				Response response = uploadManager.put(inputStream, key, upToken, null, null);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				result.put(RESULT_KEY, putRet.key);
				result.put(RESULT_HASE, putRet.hash);
				if (StringUtils.isNotBlank(putRet.key)) {
					return true;
				}
			} catch (QiniuException ex) {
				log.error("Upload qiniu error->" + ex.getMessage());
			}
		}
		return false;
	}

	/**
	 * 删除文件
	 * 
	 * @Title: uploadFileByInputStream
	 * @param bucket
	 *            bucket
	 * @param accessKey
	 *            AppKey
	 * @param secretKey
	 *            SecretId
	 * @param filePath
	 *            目标文件路径
	 * @return: boolean
	 */
	public static boolean deleteFile(String bucket, String accessKey, String secretKey, String filePath) {
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = filePath;
		Auth auth = Auth.create(accessKey, secretKey);
		if (StringUtils.isNotBlank(filePath)) {
			try {
				/// u/cms/www/201712/xxxxx.jpg
				/// 截成u/cms/www/201712/xxxxx.jpg,否则返回URL会无法访问到文件
				if (filePath.startsWith(WebConstants.SPT)) {
					key = filePath.substring(1);
				}
				BucketManager bucketManager = getBucketManager(auth);
				bucketManager.delete(bucket, key);
			} catch (QiniuException ex) {
				// 如果遇到异常，说明删除失败
				log.error("Upload qiniu error->" + ex.getMessage());
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 获取 UploadManager
	 * 
	 * @Title: getUploadManager
	 * @return: UploadManager
	 * @throws GlobalException
	 *             GlobalException
	 */
	public static UploadManager getUploadManager(String bucketArea) throws GlobalException {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = getConfiguration(bucketArea);
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		return uploadManager;
	}

	/**
	 * 获取Configuration
	 * 
	 * @Title: getConfiguration
	 * @param bucketArea
	 *            地区码简码
	 * @throws GlobalException
	 *             GlobalException
	 * @return: Configuration
	 */
	public static Configuration getConfiguration(String bucketArea) throws GlobalException {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = null;
		if (StringUtils.isNotBlank(bucketArea)) {
			if (BUCKET_AREA_HD.equals(bucketArea)) {
				cfg = new Configuration(Zone.zone0());
			} else if (BUCKET_AREA_HB.equals(bucketArea)) {
				cfg = new Configuration(Zone.zone1());
			} else if (BUCKET_AREA_HN.equals(bucketArea)) {
				cfg = new Configuration(Zone.zone2());
			} else if (BUCKET_AREA_BM.equals(bucketArea)) {
				cfg = new Configuration(Zone.zoneNa0());
			} else if (BUCKET_AREA_DNY.equals(bucketArea)) {
				cfg = new Configuration(Zone.zoneAs0());
			}
		}
		if (cfg == null) {
			String code = SysOtherErrorCodeEnum.OSS_BUCKET_AREA_ERROR.getCode();
			String msg = SysOtherErrorCodeEnum.OSS_BUCKET_AREA_ERROR.getDefaultMessage();
			throw new GlobalException(new SystemExceptionInfo(msg, code));
		}
		return cfg;
	}

	/**
	 * 获取 UploadManager
	 * 
	 * @Title: getUploadManager
	 * @return: UploadManager
	 */
	public static BucketManager getBucketManager(Auth auth) {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		// ...其他参数参考类注释
		BucketManager bucketManager = new BucketManager(auth, cfg);
		return bucketManager;
	}

	/**
	 * 获取认证token
	 * 
	 * @Title: getAuthToken
	 * @param bucket
	 *            BucketName
	 * @param accessKey
	 *            AppKey
	 * @param secretKey
	 *            SecretId
	 * @param: @return
	 * @return: String
	 */
	public static String getAuthToken(String bucket, String accessKey, String secretKey) {
		// ...生成上传凭证，然后准备上传
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		return upToken;
	}

	/**
	 * 输入流转换字节数组
	 * 
	 * @Title: toByteArray
	 * @param input
	 *            输入流
	 * @return: byte[]
	 */
	public static byte[] toByteArray(InputStream input) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		try {
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
		} catch (IOException e) {
			log.error("toByteArray error->" + e.getMessage());
		}
		return output.toByteArray();
	}

}

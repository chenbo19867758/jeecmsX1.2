package com.jeecms.common.upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.StorageClass;
import com.jeecms.common.constants.WebConstants;

/**
 * 阿里OSS上传工具类
 * 
 * @author: tom
 * @date: 2018年4月14日 下午3:29:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AliOssCloudClient {
	private static final Logger log = LoggerFactory.getLogger(AliOssCloudClient.class);

	/**
	 * 以本地文件上传到阿里OSS
	 * 
	 * @Title: uploadFileByLocal
	 * @param bucketName
	 *            bucketName
	 * @param endpoint
	 *            endpoint
	 * @param accessKeyId
	 *            accessKeyId
	 * @param accessKeySecret
	 *            accessKeySecret
	 * @param ossPath
	 *            上传目标文件目录(相对路径)
	 * @param localPath
	 *            本地上传文件目录（本地文件绝对路径）
	 * @return: boolean
	 */
	public static boolean uploadFileByLocal(String bucketName, String endpoint, String accessKeyId,
			String accessKeySecret, String ossPath, String localPath) {
		OSSClient ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret);
		// 判断bucket是否已经存在
		if (!doesBucketExist(bucketName, endpoint, accessKeyId, accessKeySecret)) {
			createBucket(bucketName, endpoint, accessKeyId, accessKeySecret);
		}
		// 上传文件
		try {
			if (StringUtils.isNotBlank(ossPath)) {
				// u/cms/www/201712/xxxxx.jpg
				String filename = ossPath;
				if (ossPath.startsWith(WebConstants.SPT)) {
					filename = ossPath.substring(1);
				}
				// 不带进度条
				ossClient.putObject(bucketName, filename, new File(localPath));
				// 关闭client
				ossClient.shutdown();
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 以字节数组形式上传阿里OSS
	 * 
	 * @Title: uploadFileByLocal
	 * @param bucketName
	 *            bucketName
	 * @param endpoint
	 *            endpoint
	 * @param accessKeyId
	 *            accessKeyId
	 * @param accessKeySecret
	 *            accessKeySecret
	 * @param ossPath
	 *            上传目标文件目录(相对路径)
	 * @param content
	 *            上传的字节数组
	 * @return: boolean
	 */
	public static boolean uploadFileByByte(String bucketName, String endpoint, String accessKeyId,
			String accessKeySecret, String ossPath, byte[] content) {
		OSSClient ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret);
		// 判断bucket是否已经存在
		if (!doesBucketExist(bucketName, endpoint, accessKeyId, accessKeySecret)) {
			createBucket(bucketName, endpoint, accessKeyId, accessKeySecret);
		}
		// 上传
		try {
			if (StringUtils.isNotBlank(ossPath)) {
				// u/cms/www/201712/xxxxx.jpg
				String filename = ossPath;
				if (ossPath.startsWith(WebConstants.SPT)) {
					filename = ossPath.substring(1);
				}
				// 不带进度条
				ossClient.putObject(bucketName, filename, new ByteArrayInputStream(content));
				// 关闭client
				ossClient.shutdown();
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 以输入流的形式上传到阿里oss
	 * 
	 * @Title: uploadFileByLocal
	 * @param bucketName
	 *            bucketName
	 * @param endpoint
	 *            endpoint
	 * @param accessKeyId
	 *            accessKeyId
	 * @param accessKeySecret
	 *            accessKeySecret
	 * @param ossPath
	 *            上传目标文件目录(相对路径)
	 * @param inputStream
	 *            上传输入流
	 * @return: boolean
	 */
	public static boolean uploadFileByInputStream(String bucketName, String endpoint, String accessKeyId,
			String accessKeySecret, String ossPath, InputStream inputStream) {
		OSSClient ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret);
		// 判断bucket是否已经存在
		if (!doesBucketExist(bucketName, endpoint, accessKeyId, accessKeySecret)) {
			createBucket(bucketName, endpoint, accessKeyId, accessKeySecret);
		}
		// 上传
		try {
			if (StringUtils.isNotBlank(ossPath)) {
				/// u/cms/www/201712/xxxxx.jpg 截成u/cms/www/201712/xxxxx.jpg
				String filename = ossPath;
				if (ossPath.startsWith(WebConstants.SPT)) {
					filename = ossPath.substring(1);
				}
				// 不带进度条
				ossClient.putObject(bucketName, filename, inputStream);
				// 关闭client
				ossClient.shutdown();
				return true;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 删除文件
	 * @Title: uploadFileByLocal
	 * @param bucketName
	 *            bucketName
	 * @param endpoint
	 *            endpoint
	 * @param accessKeyId
	 *            accessKeyId
	 * @param accessKeySecret
	 *            accessKeySecret
	 * @param filePath 上传目标文件位置
	 * @return: boolean
	 */
	public static boolean deleteFile(String bucketName, String endpoint, String accessKeyId,
			String accessKeySecret, String filePath) {
		OSSClient ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret);
		// 判断bucket是否已经存在
		if (!doesBucketExist(bucketName, endpoint, accessKeyId, accessKeySecret)) {
			return false;
		}
		// 删除
		try {
			if (StringUtils.isNotBlank(filePath)) {
				/// u/cms/www/201712/xxxxx.jpg 截成u/cms/www/201712/xxxxx.jpg
				String filename = filePath;
				if (filePath.startsWith(WebConstants.SPT)) {
					filename = filePath.substring(1);
				}
				// 不带进度条
				ossClient.deleteObject(bucketName, filename);
				// 关闭client
				ossClient.shutdown();
				return true;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 判断是否存在Bucket
	 * 
	 * @Title: doesBucketExist
	 * @param bucketName
	 *            bucketName
	 * @param endpoint
	 *            endpoint
	 * @param accessKeyId
	 *            accessKeyId
	 * @param accessKeySecret
	 *            accessKeySecret
	 * @return: boolean
	 */
	public static boolean doesBucketExist(String bucketName, String endpoint, String accessKeyId,
			String accessKeySecret) {
		OSSClient ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret);
		boolean exists = ossClient.doesBucketExist(bucketName);
		return exists;
	}

	/**
	 * 创建Bucket
	 * 
	 * @Title: createBucket
	 * @param bucketName
	 *            bucketName
	 * @param endpoint
	 *            endpoint
	 * @param accessKeyId
	 *            AppKey
	 * @param accessKeySecret
	 *            SecretId
	 * @return: void
	 */
	public static void createBucket(String bucketName, String endpoint,
			String accessKeyId, String accessKeySecret) {
		CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
		// 设置bucket权限为公共读，默认是私有读写
		createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
		// 设置bucket存储类型为低频访问类型，默认是标准类型,是否必須？？？
		createBucketRequest.setStorageClass(StorageClass.IA);
		OSSClient ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret);
		boolean exists = ossClient.doesBucketExist(bucketName);
		if (!exists) {
			try {
				ossClient.createBucket(createBucketRequest);
				// 关闭client
				ossClient.shutdown();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 获取阿里OSS client
	 * 
	 * @Title: getOSSClient
	 * @param endpoint
	 *            endpoint
	 * @param accessKeyId
	 *            AppKey
	 * @param accessKeySecret
	 *            SecretId
	 * @return: OSSClient
	 */
	public static OSSClient getOSSClient(String endpoint, String accessKeyId, String accessKeySecret) {
		// 创建ClientConfiguration实例，按照您的需要修改默认参数
		ClientConfiguration conf = new ClientConfiguration();
		// 开启支持CNAME选项
		conf.setSupportCname(true);
		// 创建OSSClient实例
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
		return client;
	}

}

package com.jeecms.common.upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.DelFolderRequest;
import com.qcloud.cos.request.ListFolderRequest;
import com.qcloud.cos.request.MoveFileRequest;
import com.qcloud.cos.request.StatFolderRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;

/**
 * 腾讯COS上传工具
 * 
 * @author: tom
 * @date: 2018年4月14日 下午4:00:16
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class QcoscloudClient {
	private static final Logger log = LoggerFactory.getLogger(QcoscloudClient.class);

	/**
	 * 本地文件上传腾讯cos
	 * @Title: uploadFileByLocal
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId  secretId
	 * @param appKey appKey
	 * @param cosPath
	 *            远程文件夹的名称
	 * @param localPath
	 *            文件的本地路径
	 * @return: String
	 */
	public static String uploadFileByLocal(String bucketArea, String bucketName, String appId, String secretId,
			String appKey, String cosPath, String localPath) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosPath, localPath);
			String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
			return uploadFileRet;
		} else {
			return "";
		}

	}

	/**
	 * 以字节数组的形式上传文件
	 * @Title: uploadFileByByte
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId secretId
	 * @param appKey appKey
	 * @param cosPath
	 *            目标路径
	 * @param content
	 *            字节数组
	 * @return: String
	 */
	public static String uploadFileByByte(String bucketArea, String bucketName, String appId, String secretId,
			String appKey, String cosPath, byte[] content) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosPath, content);
			String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
			return uploadFileRet;
		} else {
			return "";
		}
	}

	/**
	 * 上传文件 以输入流的形式
	 * @Title: uploadFileByInputStream
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId secretId
	 * @param appKey appKey
	 * @param cosPath
	 *            目标路径
	 * @param inputStream
	 *            输入流
	 * @return: String
	 */
	public static String uploadFileByInputStream(String bucketArea, String bucketName,
			String appId, String secretId,
			String appKey, String cosPath, InputStream inputStream) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName,
					cosPath, toByteArray(inputStream));
			String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
			return uploadFileRet;
		} else {
			return "";
		}
	}

	/**
	 * 移动文件
	 * @Title: moveFile
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId  secretId
	 * @param appKey appKey
	 * @param cosFilePath
	 *            原路径
	 * @param dstCosFilePath
	 *            目标路径
	 * @return: String
	 */
	public static String moveFile(String bucketArea, String bucketName, String appId,
			String secretId, String appKey,
			String cosFilePath, String dstCosFilePath) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			MoveFileRequest moveRequest = new MoveFileRequest(bucketName, cosFilePath, dstCosFilePath);
			String moveFileResult = cosClient.moveFile(moveRequest);
			return moveFileResult;
		} else {
			return "";
		}
	}

	/**
	 * 删除文件
	 * @Title: deleteFile
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId secretId
	 * @param appKey appKey
	 * @param cosFilePath
	 *            目标文件路径
	 * @return: String
	 */
	public static String deleteFile(String bucketArea, String bucketName, 
			String appId, String secretId, String appKey,String cosFilePath) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			DelFileRequest delFileRequest = new DelFileRequest(bucketName, cosFilePath);
			String delFileRet = cosClient.delFile(delFileRequest);
			return delFileRet;
		} else {
			return "";
		}
	}

	/**
	 * 查询子文件夹
	 * @Title: listFolder
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId secretId
	 * @param appKey appKey
	 * @param cosPath
	 *            目标文件夹
	 * @return: String
	 */
	public static String listFolder(String bucketArea, String bucketName, 
			String appId, String secretId, String appKey,
			String cosPath) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			ListFolderRequest listFolderRequest = new ListFolderRequest(bucketName, cosPath);
			String listFolderRet = cosClient.listFolder(listFolderRequest);
			return listFolderRet;
		} else {
			return "";
		}
	}

	/**
	 * 删除文件夹
	 * @Title: delFolder delFolder
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId secretId
	 * @param appKey appKey
	 * @param cosPath
	 *            删除目标文件夹路径
	 * @return: String
	 */
	public static String delFolder(String bucketArea, String bucketName, 
			String appId, String secretId, String appKey,String cosPath) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			DelFolderRequest delFolderRequest = new DelFolderRequest(bucketName, cosPath);
			String delFolderRet = cosClient.delFolder(delFolderRequest);
			return delFolderRet;
		} else {
			return "";
		}
	}

	/**
	 * 获取文件夹信息
	 * @Title: statFolder
	 * @param bucketArea bucketArea
	 * @param bucketName bucketName
	 * @param appId appId
	 * @param secretId secretId
	 * @param appKey appKey
	 * @param cosPath cos路径
	 * @return: String
	 */
	public static String statFolder(String bucketArea, String bucketName, 
			String appId, String secretId, String appKey,String cosPath) {
		COSClient cosClient = getCOSClient(bucketArea, appId, secretId, appKey);
		if (cosClient != null) {
			StatFolderRequest statFolderRequest = new StatFolderRequest(bucketName, cosPath);
			String statFolderRet = cosClient.statFolder(statFolderRequest);
			return statFolderRet;
		} else {
			return "";
		}
	}

	/**
	 * 获取腾讯COS Client
	 * @Title: getCOSClient
	 * @param bucketArea bucketArea
	 * @param appId  appId
	 * @param secretId secretId
	 * @param appKey appKey
	 * @return: COSClient
	 */
	public static COSClient getCOSClient(String bucketArea, String appId, String secretId, String appKey) {
		if (StringUtils.isNotBlank(appId) && StringUtils.isNumeric(appId)) {
			// 初始化秘钥信息
			Credentials cred = new Credentials(Integer.parseInt(appId), secretId, appKey);
			// 初始化客户端配置
			ClientConfig clientConfig = new ClientConfig();
			// 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
			clientConfig.setRegion(bucketArea);
			// 初始化cosClient
			COSClient cosClient = new COSClient(clientConfig, cred);
			return cosClient;
		} else {
			log.error("get cos client  error");
			return null;
		}
	}

	/**
	 * 输入流转换字节数组
	 * @Title: toByteArray  
	 * @param input  InputStream
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
			log.error(e.getMessage());
		}
		return output.toByteArray();
	}
	

}
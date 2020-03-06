package com.jeecms.common.wechat.util.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.jeecms.common.wechat.bean.MediaFile;

/**
 * 代理请求实现接口
 * @author: qqwang
 * @date: 2018年6月21日 下午3:31:55
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface HttpDelegate {

	/**
	 * 发送GET请求
	 * @Title: get  
	 * @param url 请求URL
	 * @param params 参数
	 * @return      
	 * @return: String
	 */
	String get(String url, Map<String, String> params);

	/**
	 * 发送POST请求
	 * @Title: post  
	 * @param url 请求URL
	 * @param params 参数
	 * @param postData formdata中参数
	 * @return      
	 * @return: String
	 */
	String post(String url, Map<String, String> params, String postData);

	/**
	 * POST请求
	 * @Title: post  
	 * @param url
	 * @param mchId
	 * @param certPath
	 * @param params
	 * @param postData
	 * @return      
	 * @return: String
	 */
	public String post(String url, String mchId, String certPath, Map<String, String> params, String postData);

	/**
	 * 发送POST请求(json)
	 * @Title: postForJson  
	 * @param url 请求URL
	 * @param params 参数
	 * @param postData formdata中参数
	 * @return      
	 * @return: String
	 */
	String postForJson(String url, Map<String, String> params, String postData);
	
	/**
	 * 上传文件
	 * @Title: upload  
	 * @param url 请求URL
	 * @param params 参数
	 * @param data
	 * @param file 文件
	 * @return      
	 * @return: String
	 */
	String upload(String url, Map<String, String> params, String data, File file);

	/**
	 * 下载文件（GET请求）
	 * @Title: downloadByGet  
	 * @param url 请求URL
	 * @param params 参数
	 * @return      
	 * @return: MediaFile
	 */
	MediaFile downloadByGet(String url, Map<String, String> params);

	/**
	 * 下载文件（POST请求）
	 * @Title: downloadByPost  
	 * @param url
	 * @param params
	 * @param data
	 * @return      
	 * @return: MediaFile
	 */
	MediaFile downloadByPost(String url, Map<String, String> params, String data);

	/**
	 * 发送GET请求
	 * @Title: getJsonBean  
	 * @param url 请求URL
	 * @param params 参数
	 * @param clazz 转换bean的clazz
	 * @return      
	 * @return: T
	 */
	<T> T getJsonBean(String url, Map<String, String> params, Class<T> clazz);

	/**
	 * 发送POST请求
	 * @Title: postJsonBean  
	 * @param url 请求URL
	 * @param params 参数
	 * @param postData formdata中参数
	 * @param clazz 转换bean的clazz
	 * @return      
	 * @return: T
	 */
	<T> T postJsonBean(String url, Map<String, String> params, String postData, Class<T> clazz);

	/**
	 * 上传文件自动抛出异常，全局已捕获
	 * @Title: uploadJsonBean  
	 * @param url 请求URL
	 * @param params 参数
	 * @param postData formdata中参数
	 * @param file 文件
	 * @param clazz 转换bean的clazz
	 * @return      
	 * @return: T
	 */
	<T> T uploadJsonBean(String url, Map<String, String> params, String postData, File file, Class<T> clazz);

	/**
	 * 发送GET请求权，系统会自动抛出异常，全局已捕获
	 * @Title: getXmlBean  
	 * @param url 请求URL
	 * @param params 参数
	 * @param clazz 转换bean的clazz
	 * @return      
	 * @return: T
	 */
	<T> T getXmlBean(String url, Map<String, String> params, Class<T> clazz);

	/**
	 * 发送POST请求
	 * @Title: postXmlBean  
	 * @param url 请求URL
	 * @param params 参数
	 * @param postData formdata 参数
	 * @param clazz 转换bean的clazz
	 * @return      
	 * @return: T
	 */
	<T> T postXmlBean(String url, Map<String, String> params, String postData, Class<T> clazz);

	/**
	 * 将inputstream转成byte数组
	 * @Title: readInputStream  
	 * @param inStream
	 * @return
	 * @throws IOException      
	 * @return: byte[]
	 */
	byte[] readInputStream(InputStream inStream) throws IOException;

	/**
	 * 读取网络图片转成byte数组
	 * @Title: readURLImage  
	 * @param imageUrl
	 * @return
	 * @throws IOException      
	 * @return: byte[]
	 */
	byte[] readURLImage(String imageUrl) throws IOException;

	
	/**
	 * 读取下载视频链接转成byte数组
	 * @Title: readURLIVideo  
	 * @param videoUrl
	 * @return
	 * @throws IOException      
	 * @return: byte[]
	 */
	byte[] readURLVideo(String videoUrl) throws IOException;
}

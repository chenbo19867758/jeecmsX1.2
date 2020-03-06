package com.jeecms.resource.service;

import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;

/***
 * 
 * @Description:图片service
 * @author: tom
 * @date: 2018年12月17日 上午9:48:06
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ImageService {
	/**
	 * 抓取远程图片返回本地地址
	 * 
	 * @param imgUrl
	 *            远程图片URL
	 * @return
	 */
	public String crawlImg(String imgUrl);

	/**
	 * 抓取远程图片返回本地地址
	 * @Title: crawlImg  
	 * @param imgUrl 远程图片URL
	 * @param ctx 根路径
	 * @param ftp FTP信息
	 * @param oss oss对象
	 * @param uploadPath 本地存储地址
	 * @return  本地存储图片地址
	 */
	public String crawlImg(String imgUrl, String ctx, UploadFtp ftp, UploadOss oss, String uploadPath);
}

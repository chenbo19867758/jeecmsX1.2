package com.jeecms.front.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.resource.base.BaseUeditorUploadAct;

/**
 * 编辑器上传
 * 
 * @author: tom
 * @date: 2018年12月17日 下午3:27:24
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
public class UeditorUploadAct extends BaseUeditorUploadAct {

	/**
	 * 编辑器上传
	 * 
	 * @param typeStr
	 *            类型 File文件 Flash Flash Image图片 Media视频 Audio音频
	 * @param mark
	 *            是否添加水印
	 */
	@Override
	@RequestMapping(value = "/ueditor/upload")
	public void upload(@RequestParam(value = "Type", required = false) String typeStr, Boolean mark,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.upload(typeStr, mark, request, response);
	}

	@RequestMapping(value = "/ueditor/getRemoteImage")
	@Override
	public void getRemoteImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.getRemoteImage(request, response);
	}

	/**
	 * 在线图片管理（选择最近或站点图片）
	 */
	@Override
	@RequestMapping(value = "/ueditor/imageManager")
	public void imageManager(Integer picNum, Boolean insite, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		super.imageManager(picNum, insite, request, response);
	}

	@Override
	@RequestMapping(value = "/ueditor/scrawlImage")
	public void scrawlImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.scrawlImage(request, response);
	}

}

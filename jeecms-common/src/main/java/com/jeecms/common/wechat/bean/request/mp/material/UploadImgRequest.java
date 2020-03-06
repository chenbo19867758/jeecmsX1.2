package com.jeecms.common.wechat.bean.request.mp.material;

import java.io.File;

/**
 * 
 * @Description: 上传图文消息内的图片获取URL：request
 * @author: chenming
 * @date:   2018年7月30日 下午5:57:36     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UploadImgRequest {
	
	/**
	 * 文件项目名称
	 */
	private  File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	

	
}
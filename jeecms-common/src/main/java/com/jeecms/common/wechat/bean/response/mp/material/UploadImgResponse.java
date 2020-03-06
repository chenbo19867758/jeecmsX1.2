package com.jeecms.common.wechat.bean.response.mp.material;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 上传图文消息内的图片获取URL：response
 * @author: chenming
 * @date:   2018年7月30日 下午5:57:36     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UploadImgResponse extends BaseResponse{
	/**
	 * 图片url地址
	 */
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}

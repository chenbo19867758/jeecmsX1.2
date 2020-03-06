package com.jeecms.common.wechat.bean.request.mp.material;

/**
 * 
 * @Description: 调用素材管理的通用实体
 * @author: tom
 * @date:   2018年7月27日 下午3:29:13     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BaseMaterialRequest {
	/** 媒体文件类型*/ 
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BaseMaterialRequest(String type) {
		super();
		this.type = type;
	}

	public BaseMaterialRequest() {
		super();
	}

}

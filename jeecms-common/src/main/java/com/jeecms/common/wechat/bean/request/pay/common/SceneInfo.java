package com.jeecms.common.wechat.bean.request.pay.common;

/**
 * 
 * @Description: 微信H5支付的场景值对象
 * @author: chenming
 * @date:   2018年11月12日 下午4:08:20     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SceneInfo {
	
	private Info h5Info;

	public Info getH5Info() {
		return h5Info;
	}

	public void setH5Info(Info h5Info) {
		this.h5Info = h5Info;
	}

	public SceneInfo(Info h5Info) {
		super();
		this.h5Info = h5Info;
	}

	public SceneInfo() {
		super();
	}

}

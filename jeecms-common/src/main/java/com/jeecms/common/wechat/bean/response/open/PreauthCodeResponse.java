package com.jeecms.common.wechat.bean.response.open;

import com.jeecms.common.wechat.bean.response.open.BaseOpenResponse;


/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description:该API用于获取预授权码。预授权码用于公众号或小程序授权时的第三方平台方安全验证 ,返回结构参数
 * @author: wangqq
 * @date:   2018年7月25日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class PreauthCodeResponse  extends  BaseOpenResponse{

	/**预授权码*/
	private String preAuthCode;
	/**有效期，为10分钟*/
	private Long expiresIn;
	public String getPreAuthCode() {
		return preAuthCode;
	}
	public void setPreAuthCode(String preAuthCode) {
		this.preAuthCode = preAuthCode;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
	
	
	
}

 
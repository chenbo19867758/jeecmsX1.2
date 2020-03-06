package com.jeecms.common.wechat.bean.request.open;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description: 推送component_verify_ticket协议加密数据
 * @author: wangqq
 * @date:   2018年7月25日 上午10:07:00     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml") 
public class VerifyTicketRequest{

	/**第三方平台appid*/
	@XStreamAlias("AppId")  
	private String appId;
	
	/**Ticket内容加密内容*/
	@XStreamAlias("Encrypt")
	private String encrypt;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	
}



 
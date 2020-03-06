package com.jeecms.common.wechat.bean.request.open;


/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description:该API用于获取预授权码。预授权码用于公众号或小程序授权时的第三方平台方安全验证，请求结构参数
 * @author: wangqq
 * @date:   2018年7月25日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class PreauthCodeRequest  {

	/**第三方平台appid*/
	private String componentAppid;

	public String getComponentAppid() {
		return componentAppid;
	}

	public void setComponentAppid(String componentAppid) {
		this.componentAppid = componentAppid;
	}
	
	
	
}

 
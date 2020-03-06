/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.bean.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**   
 * 基础请求
 * @author: ljw
 * @date:   2019年6月17日 下午1:48:47     
 */
public class BaseRequest {

	/** 采用OAuth授权方式为必填参数，OAuth授权后获得。 **/
	@XStreamAlias("access_token")
	private String accessToken;
	
	public BaseRequest() {}
	
	public BaseRequest(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}

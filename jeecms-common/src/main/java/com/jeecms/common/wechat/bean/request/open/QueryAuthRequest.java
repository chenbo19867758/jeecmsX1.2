package com.jeecms.common.wechat.bean.request.open;
/**
 * {@link}https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx
 * @Description:该API用于 使用授权码换取公众号或小程序的接口调用凭据和授权信息，请求结构参数
 * <p>Title:QueryAuthRequest</p>
 * @author wulongwei
 * @date 2018年7月27日
 */
public class QueryAuthRequest {

	/**第三方平台appid*/
	private String componentAppid;
	
	/**授权code,会在授权成功时返回给第三方平台，详见第三方平台授权流程说明*/
	private String authorizationCode;

	public String getComponentAppid() {
		return componentAppid;
	}

	public void setComponentAppid(String componentAppid) {
		this.componentAppid = componentAppid;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	
}

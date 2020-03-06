package com.jeecms.wechat.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 
 * @Description:微信公众号token信息
 * @author: qqwang
 * @date:   2018年6月11日 上午10:28:00     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name="jc_wechat_token")
public  class AbstractWeChatToken extends AbstractDomain<Integer> {

	private static final long serialVersionUID = 1L;
	private Integer id;
	/**公众号appid或开放平台应用appid*/
	private String appId;
	/**开放平台的component_access_token(服务开发方的access_token) */
	private String componentAccessToken;
	/**微信开放平台服务器推送的component_verify_ticket，用于刷新component_access_token，（为解密之后数据）*/
	private String componentVerifyTicket;
	/**公众号授权开放平台应用产生的token，用于刷新authorizer_access_token，授权信息发生变更，会重置*/
	private String authorizerRefreshToken;
	/**开放平台应用代为调用公众号接口所使用的token ，等同于公众平台的access_token*/
	private String authorizerAccessToken;
	/**component_access_token或authorizer_access_token产生时间*/
	private Date acceccTokenCreateTime;
	
	public AbstractWeChatToken(){}
	

	@Id
	@Override
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_wechat_token", pkColumnValue = "jc_wechat_token", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_token")
	public Integer getId() {
		return this.id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@Column(name = "component_access_token", nullable = true, length = 500)
	public String getComponentAccessToken() {
		return componentAccessToken;
	}
	public void setComponentAccessToken(String componentAccessToken) {
		this.componentAccessToken = componentAccessToken;
	}
	
	@Column(name = "component_verify_ticket", nullable = true, length = 255)
	public String getComponentVerifyTicket() {
		return componentVerifyTicket;
	}
	public void setComponentVerifyTicket(String componentVerifyTicket) {
		this.componentVerifyTicket = componentVerifyTicket;
	}
	
	@Column(name = "authorizer_refresh_token", nullable = true, length = 500)
	public String getAuthorizerRefreshToken() {
		return authorizerRefreshToken;
	}
	public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
		this.authorizerRefreshToken = authorizerRefreshToken;
	}
	

	@Column(name = "authorizer_access_token", nullable = true, length = 500)
	public String getAuthorizerAccessToken() {
		return authorizerAccessToken;
	}
	public void setAuthorizerAccessToken(String authorizerAccessToken) {
		this.authorizerAccessToken = authorizerAccessToken;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "accecc_token_create_time")
	public Date getAcceccTokenCreateTime() {
		return acceccTokenCreateTime;
	}


	public void setAcceccTokenCreateTime(Date acceccTokenCreateTime) {
		this.acceccTokenCreateTime = acceccTokenCreateTime;
	}
	
	
}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.member.domain.dto;

import javax.validation.constraints.NotNull;

/**   
 * 第三方登录Dto
 * @author: ljw
 * @date:   2019年7月20日 下午4:23:57     
 */
public class PcLoginDto {

	/**登录方式 1.直接登录 2.绑定会员登录**/
	private Integer loginWay;
	/**登录类型QQ WECHAT WEIBO **/
	private String loginType;
	/**用户第三方ID**/
	private String thirdId;
	/**用户第三方昵称**/
	private String nickname;
	/**用户名**/
	private String username;
	/**密码**/
	private String psw;
	/**站点ID**/
	private Integer siteId;
	
	public PcLoginDto(){}

	@NotNull
	public Integer getLoginWay() {
		return loginWay;
	}

	public void setLoginWay(Integer loginWay) {
		this.loginWay = loginWay;
	}

	@NotNull
	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	@NotNull
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}
	
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotNull
	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	@NotNull
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**直接登录**/
	public static final Integer TYPE_LOGIN = 1; 
	/**绑定登录**/
	public static final Integer TYPE_BIND = 2; 
}
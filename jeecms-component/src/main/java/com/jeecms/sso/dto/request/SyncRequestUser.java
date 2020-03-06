/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.sso.dto.request;

import java.io.Serializable;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.util.DesUtil;
import com.jeecms.sso.dto.BaseDto;

/**   
 * 同步用户
 * @author: ljw
 * @date:   2019年10月26日 下午4:37:34     
 */
public class SyncRequestUser extends BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 用户id **/
	private Integer userId;
	/** 用户名 **/
	private String username;
	/** 密码加密串 **/
	private String password;
	/** 加密串 **/
	private String salt;
	/** 邮箱 */
	private String userEmail;
	/** 手机号 */
	private String userPhone;
	/** 用户类别（1.管理员 2. 会员） */
	private Integer userType;

	public SyncRequestUser() {
		super();
	}
	
	/**构造函数**/
	public SyncRequestUser(CoreUser user) {
		this.username = user.getUsername();
		this.password = DesUtil.encrypt(user.getPassword(), ContentSecurityConstants.DES_KEY,
                            ContentSecurityConstants.DES_IV);
		this.salt = user.getSalt();
		this.userEmail = user.getEmail();
		this.userPhone = user.getTelephone();
		if (user.getAdmin()) {
			this.userType = 1;
		} else {
			this.userType = 2;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}

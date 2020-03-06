/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto;

import java.io.Serializable;

/**
 * 请求Dto
 * 
 * @author: ljw
 * @date: 2019年10月29日 上午10:07:30
 */
public class SyncResponseRegisterVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 回传用户Id **/
	private Integer userId;
	/**同步状态**/
	private Boolean status;

	public SyncResponseRegisterVo() {}
	
	/**构造函数**/
	public SyncResponseRegisterVo(Integer userId, Boolean status) {
		this.userId = userId;
		this.status = status;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto.response;

import java.io.Serializable;

/**
 * 响应VO
 * 
 * @author: ljw
 * @date: 2019年10月26日 下午4:27:01
 */
public class SyncResponseVo extends SyncResponseBaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 数据 **/
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}

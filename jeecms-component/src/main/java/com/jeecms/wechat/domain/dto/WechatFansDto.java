/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.wechat.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.alibaba.fastjson.annotation.JSONField;

/**   
 * 微信粉丝Dto
 * @author: ljw
 * @date:   2019年6月4日 上午9:31:05     
 */
public class WechatFansDto {

	/**微信公众号APPID**/
	private String appId;
	/** id */
	@JSONField(serialize = true)
	private Integer[] ids;

	@NotNull
	@Size(min = 1, max = 10000)
	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

	@NotNull
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}

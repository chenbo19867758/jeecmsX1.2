/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 微信推送Dto
 * 
 * @author: ljw
 * @date: 2019年7月24日 下午2:23:55
 */
public class WechatViewDto {

	/** 微信公众号APPID **/
	private String appid;
	/** 内容IDs **/
	private List<Integer> contentIds = new ArrayList<Integer>(10);

	public WechatViewDto() {
	}

	@NotNull
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	@NotEmpty
	public List<Integer> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<Integer> contentIds) {
		this.contentIds = contentIds;
	}
}

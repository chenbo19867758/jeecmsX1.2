package com.jeecms.common.wechat.bean.request.mp.user;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=ACCESS_TOKEN
 * http请求方式: POST（请使用https协议）
 * @Description:为粉丝设置标签
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UserTagsAddRequest {
	
	/**用户的openid**/
	@NotNull
	private List<String> openidList;
	
	@NotNull
	private Integer tagid;

	public List<String> getOpenidList() {
		return openidList;
	}

	public void setOpenidList(List<String> openidList) {
		this.openidList = openidList;
	}

	public Integer getTagid() {
		return tagid;
	}

	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}
		
}

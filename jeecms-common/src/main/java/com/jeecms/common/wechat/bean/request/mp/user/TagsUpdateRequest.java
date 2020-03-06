package com.jeecms.common.wechat.bean.request.mp.user;

import javax.validation.constraints.NotNull;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/tags/update?access_token=ACCESS_TOKEN
 * http请求方式: POST（请使用https协议）
 * @Description:修改一个标签
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TagsUpdateRequest {

	/**tag对象**/
	private Tag tag;

	public class Tag {
		
		/**标签名（30个字符以内）**/
		@NotNull
		private String name;
		
		/**标签Id**/
		@NotNull
		private Integer id;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}		
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}

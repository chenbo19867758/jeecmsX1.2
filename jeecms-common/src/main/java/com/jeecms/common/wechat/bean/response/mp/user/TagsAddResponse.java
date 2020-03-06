package com.jeecms.common.wechat.bean.response.mp.user;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/tags/create?access_token=ACCESS_TOKEN
 * @Description:创建一个标签的返回参数
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TagsAddResponse extends  BaseResponse{

	/**tag对象**/
	private Tag tag;

	public class Tag {
		
		/**标签id，由微信分配**/
		private Integer id;
		/**标签名（30个字符以内）**/
		private String name;
				
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}		
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}	
}

package com.jeecms.common.wechat.bean.response.mp.user;

import java.util.List;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN
 * @Description:获取公众号已创建的标签
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TagsResponse extends  BaseResponse{

	/**tag对象**/
	private List<Tag> tags;

	public class Tag {
		
		/**标签id，由微信分配**/
		private Integer id;
		/**标签名（30个字符以内）**/
		private String name;
		/**该标签下的粉丝数**/
		private Integer count;	
		
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

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}		
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.wechat.bean.response.mp.comment;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**   
 * 留言回复
 * @author: ljw
 * @date:   2019年9月7日 下午2:40:36     
 */
public class Reply {

	/** 回复时间 **/
	@XStreamAlias("create_time")
	private Long createTime;
	/** 回复评论内容 **/
	private String content;

	public Reply() {
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

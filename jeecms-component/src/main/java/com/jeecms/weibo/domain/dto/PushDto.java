/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.weibo.domain.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 微博推送文章Dto
 * 
 * @author: ljw
 * @date: 2019年6月19日 上午9:27:50
 */
public class PushDto {

	/** 内容ID **/
	private List<Integer> contentIds;
	/**站点ID**/
	private Integer siteId;
	/** 微博账户ID **/
	private List<Integer> weiboIds;

	public PushDto() {}
	
	@NotNull
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotEmpty
	public List<Integer> getWeiboIds() {
		return weiboIds;
	}

	public void setWeiboIds(List<Integer> weiboIds) {
		this.weiboIds = weiboIds;
	}
	
	@NotEmpty
	public List<Integer> getContentIds() {
		return contentIds;
	}



	public void setContentIds(List<Integer> contentIds) {
		this.contentIds = contentIds;
	}

}

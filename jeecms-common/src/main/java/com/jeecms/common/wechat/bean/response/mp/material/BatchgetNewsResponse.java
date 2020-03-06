package com.jeecms.common.wechat.bean.response.mp.material;

import java.util.List;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.jeecms.common.wechat.bean.response.mp.material.common.BatchgetNewsItem;

/**
 * 
 * @Description: 获取永久图文素材列表：response
 * @author: chenming
 * @date:   2018年7月30日 下午3:02:32     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BatchgetNewsResponse extends BaseResponse{
	private Integer totalCount;
	
	private Integer itemCount;
	
	private List<BatchgetNewsItem> item;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public List<BatchgetNewsItem> getItem() {
		return item;
	}

	public void setItem(List<BatchgetNewsItem> item) {
		this.item = item;
	}

	
	
	
}

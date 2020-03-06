package com.jeecms.common.wechat.bean.response.mp.material;

import java.util.List;

import com.jeecms.common.wechat.bean.request.mp.material.common.GetArticles;
import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 获取永久图文素材：response
 * @author: chenming
 * @date:   2018年7月30日 下午6:02:12     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetNewsResponse extends BaseResponse{
	/**
	 * 图文素材实体集合
	 */
	private List<GetArticles> newsItem;
	

	public List<GetArticles> getNewsItem() {
		return newsItem;
	}

	public void setNewsItem(List<GetArticles> newsItem) {
		this.newsItem = newsItem;
	}

}

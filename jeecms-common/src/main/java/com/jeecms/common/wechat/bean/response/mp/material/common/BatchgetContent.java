package com.jeecms.common.wechat.bean.response.mp.material.common;

import java.util.List;

import com.jeecms.common.wechat.bean.request.mp.material.common.GetArticles;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:41:41
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BatchgetContent {
	private List<GetArticles> newsItem;

	public List<GetArticles> getNewsItem() {
		return newsItem;
	}

	public void setNewsItem(List<GetArticles> newsItem) {
		this.newsItem = newsItem;
	}
	
	
}

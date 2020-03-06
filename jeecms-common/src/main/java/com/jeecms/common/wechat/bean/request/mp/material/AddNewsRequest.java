package com.jeecms.common.wechat.bean.request.mp.material;

import java.util.List;

import com.jeecms.common.wechat.bean.request.mp.material.common.SaveArticles;
/**
 * 
 * @Description:新增图文素材
 * @author: chenming
 * @date:   2018年7月30日 上午9:35:33     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AddNewsRequest {
	/** 图文消息集合 */
	private List<SaveArticles> articles;
	
	public List<SaveArticles> getArticles() {
		return articles;
	}

	public void setArticles(List<SaveArticles> articles) {
		this.articles = articles;
	}

	

	public AddNewsRequest(List<SaveArticles> articles) {
		super();
		this.articles = articles;
	}

	public AddNewsRequest() {
		super();
	}

}

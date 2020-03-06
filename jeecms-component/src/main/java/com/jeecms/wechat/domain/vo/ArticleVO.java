/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.wechat.domain.vo;

import com.jeecms.wechat.domain.WechatSendArticle;

/**   
 * 群发文章VO
 * @author: ljw
 * @date:   2019年6月5日 上午11:40:44     
 */
public class ArticleVO {

	/**文章列表**/
	private WechatSendArticle article;
	/**未读角标**/
	private Integer unread;
	/**留言条数**/
	private Integer comments;
	
	public ArticleVO() {}

	public WechatSendArticle getArticle() {
		return article;
	}

	public void setArticle(WechatSendArticle article) {
		this.article = article;
	}

	public Integer getUnread() {
		return unread;
	}

	public void setUnread(Integer unread) {
		this.unread = unread;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}
	
}

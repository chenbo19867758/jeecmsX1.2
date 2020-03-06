/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.article;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.weibo.bean.request.article.ArticlePushRequest;
import com.jeecms.common.weibo.bean.response.article.ArticlePushResponse;

/**   
 * 微博文章推送
 * @author: ljw
 * @date:   2019年6月18日 下午4:47:19     
 */
public interface WeiboArticleService {

	/**
	 * 微博文章推送
	* @Title: push 
	* @param request 请求内容
	* @return ArticlePushResponse 响应
	* @throws GlobalException 异常
	 */
	ArticlePushResponse push(ArticlePushRequest request) throws GlobalException;
}

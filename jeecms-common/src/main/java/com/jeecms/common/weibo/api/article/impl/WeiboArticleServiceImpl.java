/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.article.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.api.article.WeiboArticleService;
import com.jeecms.common.weibo.bean.request.article.ArticlePushRequest;
import com.jeecms.common.weibo.bean.response.article.ArticlePushResponse;

/**   
 * 微博文章实现类
 * @author: ljw
 * @date:   2019年6月18日 下午4:51:43     
 */
@Service
public class WeiboArticleServiceImpl implements WeiboArticleService {
	
	/** 推送文章接口 **/
	public static final String PUSH_URL = "https://api.weibo.com/proxy/article/publish.json";
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String COVER = "cover";
	public static final String SUMMARY = "summary";
	public static final String TEXT = "text";

	@Override
	public ArticlePushResponse push(ArticlePushRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(TITLE, request.getTitle());
		params.put(CONTENT, request.getContent());
		params.put(COVER, request.getCover());
		params.put(SUMMARY, request.getSummary());
		params.put(TEXT, request.getText());
		ArticlePushResponse response = HttpUtil.postJsonBean(PUSH_URL, params, null, ArticlePushResponse.class);
		return response;
	}

}

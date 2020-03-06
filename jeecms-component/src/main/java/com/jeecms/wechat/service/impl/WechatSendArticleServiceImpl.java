/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.wechat.dao.WechatSendArticleDao;
import com.jeecms.wechat.domain.WechatSendArticle;
import com.jeecms.wechat.service.WechatCommentService;
import com.jeecms.wechat.service.WechatSendArticleService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;

/**
 * 实现类
* @author ljw
* @version 1.0
* @date 2019-06-04
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatSendArticleServiceImpl extends BaseServiceImpl<WechatSendArticle,WechatSendArticleDao, Integer>  
			implements WechatSendArticleService {

	@Autowired
	private WechatCommentService  wechatCommentService;
	
	@Override
	public List<WechatSendArticle> getArticles(List<String> appids, Date start, Date end) throws GlobalException {
		return dao.selectArts(appids, start, end);
	}
	
	@Override
	public List<WechatSendArticle> getArticlesBySend(List<String> msgDataIds, Date start, Date end) 
			throws GlobalException {
		return dao.selectArtsBySend(msgDataIds, start, end);
	}

	@Override
	public WechatSendArticle findArticle(String msgDataId, Integer index) throws GlobalException {
		return dao.findByMsgDataIdAndMsgDataIndex(msgDataId,index);
	}

	@Override
	public void read(String msgDataId, Integer index) throws GlobalException {
		//处理已读
		Long userCommentId = wechatCommentService.getUserCommentId(msgDataId,index);
		if (userCommentId != null) {
			WechatSendArticle article = findArticle(msgDataId, index);
			article.setMaxUserCommentId(userCommentId.intValue());
			super.update(article);	
		}
	}

 
}
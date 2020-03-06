package com.jeecms.wechat.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.WechatReplyContentDaoExt;
import com.jeecms.wechat.domain.WechatReplyContent;


/**
* @author ASUS
* @version 1.0
* @date 2018-08-08
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/
public interface WechatReplyContentDao extends IBaseDao<WechatReplyContent, Integer>,WechatReplyContentDaoExt {
	
	WechatReplyContent findByIdAndHasDeleted(Integer id,Boolean hasDeleted);
}

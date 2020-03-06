package com.jeecms.wechat.dao;


import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.AbstractWeChatOpen;

/**
 * @Description:AbstractWeChatToken 
 * @author: qqwang
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface AbstractWeChatOpenDao extends IBaseDao<AbstractWeChatOpen, Integer> {
	
	/**
	 * 查找已删除WeChatOpen
	 * @Title: findByHasDeleted  
	 * @param hasDeleted 是否删除
	 * @return      
	 * @return: AbstractWeChatOpen
	 */
	AbstractWeChatOpen findByHasDeleted(Boolean hasDeleted);
}

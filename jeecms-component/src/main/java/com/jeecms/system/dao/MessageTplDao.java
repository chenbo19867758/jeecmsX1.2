/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.MessageTpl;

import java.lang.String;

/**
 * 模板信息 dao接口
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年4月26日 下午12:00:13
 */
public interface MessageTplDao extends IBaseDao<MessageTpl, Integer> {
	
	/**
	 * 验证模板标识是否唯一
	 * @Title: findByMesCodeAndHasDeleted  
	 * @param mesCode
	 * @param hasDeleted 是否删除
	 * @return      
	 * @return: MessageTpl
	 */
    MessageTpl findByMesCodeAndHasDeleted(String mesCode,Boolean hasDeleted);

}
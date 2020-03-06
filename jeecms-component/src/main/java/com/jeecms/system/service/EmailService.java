package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.Email;

/**
 * email的service接口
 * @author: chenming
 * @date:   2019年4月12日 下午6:17:14
 */
public interface EmailService extends IBaseService<Email, Integer> {
	
	/**
	 * 查找开启的邮箱接口
	 * @Title: findDefault  
	 * @throws GlobalException 程序异常     
	 * @return: Email
	 */
	Email findDefault() throws GlobalException;
	
	/**
	 * 查询数据库的唯一的邮箱接口
	 * @Title: findOnly  
	 * @throws GlobalException   程序异常   
	 * @return: Email
	 */
	Email findOnly() throws GlobalException;
	
	/**
	 * 保存邮箱
	 * @Title: save 
	 * @param email 邮件设置对象
	 * @throws GlobalException   程序异常      
	 * @return: Email
	 */
	Email save(Email email) throws GlobalException;
	
	/**
	 * 修改邮箱
	 * @Title: update 
	 * @param email 邮件设置对象
	 * @throws GlobalException  程序异常       
	 * @return: Email
	 */
	Email update(Email email) throws GlobalException;
}

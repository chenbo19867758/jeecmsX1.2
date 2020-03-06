package com.jeecms.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.Email;

/**
 * 短信服务接口
 * @author: chenming
 * @date:   2019年4月13日 上午11:32:31
 */
public interface EmailDao extends JpaRepository<Email, Integer>,IBaseDao<Email, Integer> {
	
	/**
	 * 查询全局开启的服务
	 * @Title: findByisGloable  
	 * @param isGloable		是否全局配置
	 * @return: Email
	 */
	Email findByisGloable(Boolean isGloable);
}

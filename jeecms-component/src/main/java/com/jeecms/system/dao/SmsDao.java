package com.jeecms.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.Sms;

/**
 * 短信设置dao接口
 * @author: chenming
 * @date:   2019年4月15日 上午10:25:35
 */
public interface SmsDao extends JpaRepository<Sms, Integer>,IBaseDao<Sms, Integer> {
	
	/**
	 * 获取全局配置
	 * @Title: findByisGloable  
	 * @param isGloable		是否全局开启
	 * @return: Sms
	 */
	Sms findByisGloable(Boolean isGloable);
	
}

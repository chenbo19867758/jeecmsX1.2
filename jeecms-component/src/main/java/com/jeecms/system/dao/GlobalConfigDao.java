/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.dao;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.GlobalConfig;


/**
 * GlobalConfig dao查询接口
 * @author: wulongwei
 * @date:   2019年4月13日 下午4:09:24     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface GlobalConfigDao extends IBaseDao<GlobalConfig, Integer> {
	
	/**
	 * 根据id获取配置信息
	 * @Title: findByIdAndHasDeleted  
	 * @param id 主键ID
	 * @param hasDeleted 是否删除
	 * @return
	 * @throws GlobalException 程序异常     
	 * @return: GlobalConfig 配置信息
	 */
	//GlobalConfig findByIdAndHasDeleted(Integer id,Boolean hasDeleted) throws GlobalException;
	/**
	 * 查看数据库是否有配置信息
	 * @Title: findByHasDeleted  
	 * @param hasDeleted
	 * @return
	 * @throws GlobalException      
	 * @return: GlobalConfig
	 */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	GlobalConfig findByHasDeleted(Boolean hasDeleted) throws GlobalException;
	
}

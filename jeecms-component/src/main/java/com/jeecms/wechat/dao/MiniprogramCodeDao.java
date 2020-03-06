package com.jeecms.wechat.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.MiniprogramCode;

/**
 * 草稿箱模板库dao层
 * @author: chenming
 * @date:   2019年6月12日 下午3:00:11
 */
public interface MiniprogramCodeDao extends IBaseDao<MiniprogramCode, Integer> {

	/**
	 * 获取最新的模板
	 * @Title: getNew  
	 * @return: MiniprogramCode
	 */
	@Query("select bean from MiniprogramCode bean where 1 = 1 and bean.codeType=2 and bean.isNew = 1 and bean.hasDeleted = 0")
	MiniprogramCode getNew();

	/**
	 * 删除所有草稿箱数据
	 * 
	 * @Function: MiniprogramCodeDao.java
	 * @Description:
	 * @author: wulongwei
	 * @date: 2018年11月1日 下午2:15:05
	 */
	@Modifying
	@Query("delete from MiniprogramCode bean where 1 = 1 and bean.codeType=1 and bean.hasDeleted = 0")
	void deleteDraft();

	/**
	 * 删除所有模版箱数据
	 * 
	 * @Function: MiniprogramCodeDao.java
	 * @Description:
	 * @author: wulongwei
	 * @date: 2018年11月1日 下午2:15:05
	 */
	@Modifying
	@Query("delete  from MiniprogramCode bean where 1 = 1 and bean.codeType=2  and bean.hasDeleted = 0")
	void deleteTemplate();

	/**
	 * 获取模板ID最大的数据
	 * @Title: getCode  
	 * @return: MiniprogramCode
	 */
	@Query("select bean from MiniprogramCode bean where  bean.hasDeleted = 0 and bean.templateId = (select max(bean2.templateId) from MiniprogramCode bean2)")
	MiniprogramCode getCode();

}

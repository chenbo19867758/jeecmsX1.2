/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.SysLogConfig;
import org.springframework.data.jpa.repository.Query;


/**
 * 日志策略Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 10:52:24
 */
public interface SysLogConfigDao extends IBaseDao<SysLogConfig, Integer> {

	/**
	 * 获取日志表大小(mysql)
	 *
	 * @param database 数据库名称
	 * @return 日志表大小
	 */
	@Query(nativeQuery = true, value = "SELECT concat( round( sum( DATA_LENGTH /1024 / 1024), 2 ), 'M' ) FROM "
			+ "information_schema.TABLES WHERE table_schema = ?1 AND table_name ='jc_sys_log'")
	String getMySqlTables(String database);

	/**
	 * 获取日志表大小(oracle)
	 *
	 * @return 日志表大小
	 */
	@Query(nativeQuery = true, value = "select concat(round(bytes/1024/1024, 2), 'M') from user_segments where segment_name='JC_SYS_LOG'")
	String getOracleTables();

	/**
	 * 获取日志表大小(sqlserver)
	 *
	 * @param database 数据库名称
	 * @return 日志表大小
	 */
	@Query(nativeQuery = true, value = "exec sp_spaceused 'jc_sys_log'")
	Object getSqlServerTables(String database);

	/**
	 * 获取日志表大小(达梦数据库)
	 *
	 * @param database 数据库名
	 * @return 日志表大小
	 */
	@Query(nativeQuery = true, value = "select TABLE_USED_SPACE(?1,'JC_SYS_LOG')*page()/1024/1024.0  from dual")
	String getDmTables(String database);
}

package com.jeecms.wechat.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.MiniprogramVersionDaoExt;
import com.jeecms.wechat.domain.MiniprogramVersion;


/**
 * 
 * @Description: 小程序代码版本管理dao接口
 * @author: chenming
 * @date:   2018年11月1日 上午9:16:37     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MiniprogramVersionDao extends IBaseDao<MiniprogramVersion, Integer>,MiniprogramVersionDaoExt{
	
	/**
	 * 当用户提交审核时，删除所有的该appid的审核数据，当用户提交发布是，删除所有的该appid的发布数据
	 * @Title: deleteVersion  
	 * @param appId 
	 * @param type 类型
	 * @return: void
	 */
	@Query(value = "delete from MiniprogramVersion bean where bean.appId=?1 and bean.versionType=?2")
	@Modifying
	void deleteVersion(String appId,Integer type);
	
	/**
	 * 查看当前上线的小程序代码的版本号
	 * @Title: getReleaseVersion  
	 * @param appId
	 * @return      
	 * @return: String
	 */
	@Query("select bean.codeVersion from MiniprogramVersion bean where 1 = 1 and bean.versionType=3 and bean.hasDeleted = 0 and bean.appId=?1")
	String getReleaseVersion(String appId);
	
	/**
	 * 查询审核/发布记录(经处理，审核/发布记录有1或者0条)
	 * @Title: getAudit  
	 * @param appId
	 * @param type 类型
	 * @return      
	 * @return: MiniprogramVersion
	 */
	@Query("select bean from MiniprogramVersion bean where 1 = 1 and bean.versionType=?2 and bean.hasDeleted = 0 and bean.appId=?1")
	MiniprogramVersion getAudit(String appId,Integer type);
	
	/**
	 * 拥有状态，查询按钮是否显示
	 * @Title: haveState  
	 * @param appId
	 * @param type 类型
	 * @param status 状态
	 * @return      
	 * @return: MiniprogramVersion
	 */
	@Query("select bean from MiniprogramVersion bean where 1 = 1 and bean.versionType=?2 and bean.hasDeleted = 0 and bean.appId=?1 and bean.status=?3")
	MiniprogramVersion haveState(String appId,Integer type,Integer status);
	
	/**
	 * 没有状态，查询按钮是否显示
	 * @Title: noteState  
	 * @param appId
	 * @param type 类型
	 * @return      
	 * @return: MiniprogramVersion
	 */
	@Query("select bean from MiniprogramVersion bean where 1 = 1 and bean.versionType=?2 and bean.hasDeleted = 0 and bean.appId=?1")
	MiniprogramVersion noteState(String appId,Integer type);
}

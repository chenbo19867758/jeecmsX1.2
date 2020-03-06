/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.system.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.SysBlackListDaoExt;
import com.jeecms.system.domain.SysBlackList;

/**
 * 黑名单dao接口
 * @author: chenming
 * @date:   2019年5月5日 下午6:02:50
 */
public interface SysBlackListDao extends IBaseDao<SysBlackList, Integer>,SysBlackListDaoExt {
	
	/**
	 * 底下所有的方法我都没有使用判断deleted_flag因为我直接在实体类哪里加了
	 */
	
	/**
	 * 根据站点Id和type状态查询出黑名单数据
	 * @Title: findBySiteIdAndType  
	 * @param siteId	站点Id
	 * @param type	状态
	 * @return: List
	 */
	List<SysBlackList> findBySiteIdAndType(Integer siteId,Integer type);
	
	/**
	 * 根据站点Id、状态、userId查询出精确结果
	 * @Title: findBySiteIdAndTypeAndUserId  
	 * @param siteId	站点Id
	 * @param type	状态
	 * @param userId	用户Id
	 * @return: SysBlackList
	 */
	SysBlackList findBySiteIdAndTypeAndUserId(Integer siteId, Integer type, Integer userId);
	
	/**
	 *根据站点Id、状态、ip查询出精确结果
	 * @Title: findBySiteIdAndTypeAndIp  
	 * @param siteId	站点id
	 * @param type	状态
	 * @param ip	ip
	 * @return: SysBlackList
	 */
	SysBlackList findBySiteIdAndTypeAndIp(Integer siteId, Integer type, String ip);
	
}

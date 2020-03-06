package com.jeecms.system.dao.ext;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.system.domain.SysBlackList;

/**
 * 黑名单扩展Dao
 * @author: chenming
 * @date:   2019年5月8日 上午11:00:03
 */
public interface SysBlackListDaoExt {
	/**
	 * 根据站点id，ip或用户Id查询出黑名单集合
	 * @Title: findByIpAndUserId  
	 * @param siteId	站点Id
	 * @param ip	ip
	 * @param userId	用户Id
	 * @return: List
	 */
	List<SysBlackList> findByIpAndUserId(Integer siteId,String ip,Integer userId);
	
	/**
	 * 分页查询
	 * @Title: getPage  
	 * @param status	状态
	 * @param userName	用户username进行模糊查询
	 * @param ip	用户ip模糊查询
	 * @param siteId	站点Id
	 * @param pageable	分页对象
	 * @return: Page
	 */
	Page<SysBlackList> getPage(boolean status, String userName, String ip, Integer siteId,Pageable pageable);
}

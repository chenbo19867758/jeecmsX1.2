package com.jeecms.system.service;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.vo.CmsSiteVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 站点service接口
 * @author: tom
 * @date: 2018年11月5日 下午1:57:59
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsSiteService extends IBaseService<CmsSite, Integer> {
      
	/**
	 * 查询站点列表
	 * 
	 * @Title: findAll
	 * @param isCycle   是否是回收站站点
	 * @param cacheable 是否使用缓存
	 * @return: List 列表
	 */
	List<CmsSite> findAll(Boolean isCycle, boolean cacheable);

	/**
	 * 根据父级ID查找站点列表
	 * 
	 * @Title: findByParentId
	 * @param parentId 父级ID
	 * @return: List 站点列表
	 */
	List<CmsSite> findByParentId(Integer parentId);

	/**
	 * 根据域名查找站点
	 * @Title: findByDomain  
	 * @param domain 域名
	 * @return: CmsSite 站点
	 */
	List<CmsSite> findByDomain(String domain);

	/**
	 * 根据路径查找站点你
	 * @Title: findByPath  
	 * @param path 路径
	 * @return: CmsSite 站点
	 */
	CmsSite findByPath(String path);

	/**
	 * 判断站点路径是否存在
	 * @Title: existSitePath  
	 * @param siteId 站点ID
	 * @param path 路径
	 * @return: boolean
	 */
	boolean existSitePath(Integer siteId, String path);
	
	/**
	 * 保存
	 * @Title: save  
	 * @param currSite 当前站点
	 * @param currUser 用户
	 * @param bean 站点
	 * @throws GlobalException  GlobalException     
	 * @return: CmsSite
	 */
	CmsSite save(CmsSite currSite, CoreUser currUser, CmsSite bean) throws GlobalException;
	
	/**
	 * 更新站点配置
	 * @Title: updateSiteConfig
	 * @param site 站点
	 * @param config 站点配置
	 * @return: CmsSiteConfig
	 * @throws  GlobalException GlobalException
	 *CmsSiteConfig updateSiteConfig(CmsSite site, CmsSiteConfig config) throws GlobalException;
	 */

	/**
	 * 更新站点配置
	 * @param siteId 站点id
	 * @param attr 配置
	 */
	void updateAttr(Integer siteId, Map<String, String> attr);
	
	/**
	 * 获取模板下拉列表
	 * 
	 * @Title: models
	 * @param siteId   站点ID
	 * @param solution 模板方案
	 * @return ResponseInfo
	 * @throws GlobalException GlobalException
	 */
	ResponseInfo models(Integer siteId, String solution) throws GlobalException;
	
	/**
	 * 更新列表拖动排序
	 * @param sortDto 传输DTO
	 * @param cmsList 站点列表
	 * @throws GlobalException 异常
	 */
	void updatePriority(DragSortDto sortDto, List<CmsSite> cmsList) throws GlobalException;
	
	/**
	 * 默认模板详情
	* @Title: getModel 
	* @param id 站点ID
	* @throws GlobalException 异常
	 */
	CmsSiteVo getModel(Integer id) throws GlobalException;
	
	CmsSite getCurrSite(HttpServletRequest request, HttpServletResponse response)throws GlobalException;

	List<CmsSite>  findByIds(Collection<Integer> ids);
}

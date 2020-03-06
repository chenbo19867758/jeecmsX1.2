/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.dto.CmsModelDto;

/**
 * 模型service层
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年4月17日 下午3:15:33
 */
public interface CmsModelService extends IBaseService<CmsModel, Integer> {
	/**
	 * 获得模型列表
	 * 
	 * @Title: getList 获得模型列表
	 * @param containDisabled 是否禁用
	 * @param siteId          站点ID
	 * @return 模型列表
	 */
	List<CmsModel> getList(boolean containDisabled, Integer siteId);

	/**
	 * 获得默认模型
	 * 
	 * @Title: getDefModel
	 * @param siteId 站点ID
	 * @return CmsModel 模型对象
	 */
	CmsModel getDefModel(Integer siteId);

	/**
	 * 更新列表拖动排序
	 * @param sortDto
	 * @return
	 * @throws GlobalException
	 */
	void updatePriority(DragSortDto sortDto) throws GlobalException;

	/**
	 * 获取模型分页信息
	 * 
	 * @Title: getModelPage
	 * @param tplType
	 * @param isGlobal
	 * @param isDisable
	 * @param modelName
	 * @param siteId
	 * @param pageable
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	ResponseInfo getModelPage(Short tplType, Short isGlobal, Boolean isDisable, String modelName, Integer siteId,
			Pageable pageable) throws GlobalException;

	/**
	 * 保存本站模型
	 * @Title: saveThisSiteModel  
	 * @param model
	 * @return
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 */
	ResponseInfo saveThisSiteModel(CmsModel model) throws GlobalException;

	/**
	 * 保存全站模型
	 * 
	 * @Title: saveWholeSiteModel
	 * @param model 
	 * @throws GlobalException 全局异常
	 * @return: ResponseInfo
	 */
	ResponseInfo saveWholeSiteModel(CmsModel model) throws GlobalException;

	/**
	 * 修改模型
	 * 
	 * @Title: updateModel
	 * @param model
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	ResponseInfo updateModel(CmsModel model) throws GlobalException;

	/**
	 * 是否启用
	 * 
	 * @Title: isEnable
	 * @param model
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	ResponseInfo isEnable(CmsModelDto model) throws GlobalException;

	/**
	 * 校验modelName已经存在
	 * 
	 * @Title: checkModelName
	 * @param id
	 * @param tplType
	 * @param modelName
	 * @param siteId
	 * @param isGlobal
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	ResponseInfo checkModelName(Integer id ,Short tplType, String modelName,Integer siteId,Short isGlobal) throws GlobalException;

	/**
	 * 根据条件查询出未禁用的模型列表
	 * 
	 * @Title: findList
	 * @param tplType 模型类型（1-栏目模型 2-内容模型  3-会员模型）
	 * @param siteId  站点Id
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<CmsModel> findList(Short tplType, Integer siteId) throws GlobalException;
	
	/**
	 *  获取模型详情，组合前端数据
	 * @param id 为null时，默认获取会员模型
	 * @return
	 */
	CmsModel getInfo(Integer id) throws GlobalException;
	
	/**
	 * 
	 * @param tplType
	 * @param isDisable
	 * @param siteId
	 * @return
	 * @throws GlobalException
	 */
	List<CmsModel> getModelList(Short tplType, Boolean isEnable, Integer siteId) throws GlobalException ;
	
	/**
	 * 获取栏目及内容模型字段，默认过滤模式为 SHOW_CHANNEL_AND_CONTENT
	 * @param id  
	 * @param filterModel
	 * @return
	 * @throws GlobalException
	 */
	public CmsModel getChannelOrContentModel(Integer id ) throws GlobalException ;
	
	/**
	 * 获取会员模型字段，主要应用至前端注册页面，默认过滤模式为 {@link CmsModelServiceImpl#FilterMode#SHOW_MEMBER_REGISTOR} 
	 * @throws GlobalException
	 */
	public CmsModel getFrontMemberModel() throws GlobalException ;
	
}

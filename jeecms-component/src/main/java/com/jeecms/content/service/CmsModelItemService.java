/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.vo.ModelItemDto;
import com.jeecms.system.domain.GlobalConfig;

/**
 * 模型字段service层
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年4月17日 下午3:15:13
 */
public interface CmsModelItemService extends IBaseService<CmsModelItem, Integer> {

	/**
	 * 保存模型字段
	 * @Title: saveCmsModelItem
	 * @param  ModelItemDto
	 * @throws GlobalException	全局异常
	 * @return: ResponseInfo
	 */
	ResponseInfo saveCmsModelItem(ModelItemDto modelItemDto) throws GlobalException;

	/**
	 * 根据模型Id查询模型字段List集合
	 * 
	 * @Title: findList
	 * @param modelId 模型Id
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<CmsModelItem> findByModelId(Integer modelId) throws GlobalException;

	/**
	 * 根据模型Id，类型查询模型字段List集合
	 * 
	 * @Title: findByModelIdAndType
	 * @param modelId 模型Id
	 * @param type    分组信息
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<CmsModelItem> findByModelIdAndType(Integer modelId, String type) throws GlobalException;
	
	/**
	 * 根据模型id模型字段
	 * @param modelId 模型
	 * @return
	 */
	JSONArray getModelItemByModelId(Integer modelId);
	
	/**
	 * 获取会员模型字段
	 * @return
	 */
	JSONArray getModelItemByMemberModel();
	
	/**
	 * 初始化模型字段集合
	 * @Title: initializeCmsModelItem  
	 * @param modelItems	模型字段集合
	 * @param globalConfig	系统配置
	 * @return: List
	 */
	List<CmsModelItem> initializeCmsModelItem(List<CmsModelItem> modelItems, GlobalConfig globalConfig);
	
	/**
	 * 根据模型Id，数据类型查询模型字段List集合
	 * @Title: findByModelIdAndDataType  
	 * @param modelId	模型id
	 * @param type	数据类型
	 * @throws GlobalException    全局异常  
	 * @return: List
	 */
	List<CmsModelItem> findByModelIdAndDataType(Integer modelId, String type) throws GlobalException;
	
	/**
	 * 采集通过模型id获取到该模型字段的list对象
	 * @Title: collectionModelItem  
	 * @param modelId	模型id
	 * @throws GlobalException      全局异常
	 * @return: List
	 */
	List<CmsModelItem> collectionModelItem(Integer modelId) throws GlobalException;
	
	/**
	 * 根据模型id和字段名称查询数据类型
	 * @Title: getDataType  
	 * @param modelId		模型id
	 * @param field			字段名称
	 * @throws GlobalException  全局异常    
	 * @return: String
	 */
	Map<String,String> getDataType(Integer modelId,List<String> fields) throws GlobalException;
}

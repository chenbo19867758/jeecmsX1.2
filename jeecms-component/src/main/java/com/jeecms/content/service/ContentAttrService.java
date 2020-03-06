/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.content.service;

import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.system.domain.GlobalConfig;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 内容自定义属性表Service
* @author ljw
* @version 1.0
* @date 2019-05-15
*/
public interface ContentAttrService extends IBaseService<ContentAttr, Integer> {
	
	/**
	 * 通过内容Id删除内容自定义属性
	 * @Title: deleteByContent  
	 * @param contentId	内容Id
	 * @throws GlobalException	全局异常      
	 * @return: void
	 */
	void deleteByContent(Integer contentId) throws GlobalException;
	
	/**
	 * 用于新增或修改content中组装判断contentAtt数据
	 * @Title: initContentAttr  
	 * @param contentAttrs	内容自定义属性集合
	 * @param contentId	内容id
	 * @param globalConfig	全局配置
	 * @throws GlobalException     全局异常 
	 * @return: List
	 */
	List<ContentAttr> initContentAttr(List<ContentAttr> contentAttrs,Integer contentId,GlobalConfig globalConfig) 
			throws GlobalException;
	
	/**
	 * 用于copy content组装处理判断contentAttr数据
	 * @Title: copyInitContentAttr  
	 * @param contentAttrs	contentAttrs list集合
	 * @param contentId	内容id
	 * @return: List
	 */
	List<ContentAttr> copyInitContentAttr(List<ContentAttr> contentAttrs,Integer contentId) throws GlobalException;
	
	/**
	 * 推送到站群组装contentAttr集合数据
	 * @Title: pushSiteInitContentAttr  
	 * @param contentAttrs		contentAttr集合
	 * @param contentId		内容id
	 * @param itemFiles		模型字段集合
	 * @param globalConfig	全局设置
	 * @throws GlobalException     全局异常
	 * @return: List
	 */
	List<ContentAttr> pushSiteInitContentAttr(List<ContentAttr> contentAttrs,Integer contentId,
			List<String> itemFiles,GlobalConfig globalConfig)  throws GlobalException;

	
	List<ContentAttr> initContentAttr(JSONObject json,Integer modelId)  throws GlobalException;
	
	/**
	 * 用于copy或推送到站群时，新增contentAttr
	 * @Title: copySaveContentAttr  
	 * @param contentAttrs	组装好的contentAttrs
	 * @param bean			内容
	 * @throws GlobalException    全局异常  
	 * @return: Content
	 */
	Content copySaveContentAttr(List<ContentAttr> contentAttrs,Content bean) throws GlobalException;
}

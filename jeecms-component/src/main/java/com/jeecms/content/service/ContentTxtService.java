/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.content.service;

import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentTxt;
import com.jeecms.content.domain.dto.ContentSaveDto;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 内容正文实体类Service
* @author ljw
* @version 1.0
* @date 2019-05-15
*/
public interface ContentTxtService extends IBaseService<ContentTxt, Integer> {

	/**
	 * 新增内容文本内容
	 * @Title: saveTxts  
	 * @param contentTxts	文本内容list集合
	 * @param content	内容对象
	 * @throws GlobalException   全局异常   
	 * @return: List
	 */
	List<ContentTxt> saveTxts(List<ContentTxt> contentTxts,Content content) throws GlobalException;
	
	/**
	 * 通过内容id删除该内容的文本内容
	 * @Title: deleteTxts  
	 * @param contentId	内容id
	 * @throws GlobalException	全局异常      
	 * @return: void
	 */
	void deleteTxts(Integer contentId) throws GlobalException;
	
	/**
	 * 新增内容时初始化栏目txt
	 * @Title: initContentTxt  
	 * @param json	前台传入json
	 * @param modelId	内容模型id
	 * @throws GlobalException      全局异常
	 * @return: Map
	 */
	Map<String,String> initContentTxt(JSONObject json, Integer modelId,ContentSaveDto dto,boolean isUpdate) throws GlobalException;
	
	
	List<ContentTxt> getTxts(Integer contentId);
}

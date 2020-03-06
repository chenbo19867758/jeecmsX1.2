package com.jeecms.common.wechat.api.applet;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.response.applet.BaseAppletResponse;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateDraftListResponse;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateListResponse;

/**
 * 
 * @author: tom
 * @date:   2019年3月8日 下午4:36:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface GetTemplateDraftApiService {

	/**
	 * 获取草稿箱内的所有临时代码草稿
	 * @Title: getTemplateDraftList  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetTemplateDraftListResponse
	 * @author: wulongwei
	 * @date: 2018年11月1日 上午11:36:34
	 */
	GetTemplateDraftListResponse getTemplateDraftList(ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 将草稿箱的草稿选为小程序代码模版
	 * @Title: addToTemplate  
	 * @param validateToken
	 * @param draftId 草稿ID
	 * @return
	 * @throws GlobalException      
	 * @return: BaseAppletResponse
	 * @author: wulongwei
	 * @date: 2018年11月1日 上午11:49:22
	 */
	BaseAppletResponse addToTemplate(ValidateToken validateToken,Integer draftId) throws GlobalException;

	/**
	 * 获取代码模版库中的所有小程序代码模版
	 * @Title: getTemplateList  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetTemplateListResponse
	 * @author: wulongwei
	 * @date: 2018年11月1日 下午12:13:34
	 */
	GetTemplateListResponse getTemplateList(ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 删除指定小程序代码模版
	 * @Title: deletetemplate  
	 * @param validateToken
	 * @param templateId
	 * @return
	 * @throws GlobalException      
	 * @return: BaseAppletResponse
	 * @author: wulongwei
	 * @date: 2018年11月1日 下午1:32:41
	 */
	BaseAppletResponse deletetemplate(ValidateToken validateToken,Integer templateId) throws GlobalException;
}

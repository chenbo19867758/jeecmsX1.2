/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.ContentExt;
import com.jeecms.content.domain.dto.ContentContributeDto;

/**
 * 内容扩展service接口
 * @author: chenming
 * @date:   2019年5月15日 下午5:24:08
 */
public interface ContentExtService extends IBaseService<ContentExt, Integer> {
	
	
	/**
	 * 初始化contentExt，用于：采集数据导出至内容
	 * @Title: initContentExt  
	 * @param contentExt	用户扩展对象
	 * @param siteId	站点id
	 * @param json	采集到的json串
	 * @param channelId	站点id
	 * @param modelId	模型id
	 * @throws GlobalException     全局异常 
	 * @return: ContentExt
	 */
	ContentExt initContentExt(ContentExt contentExt,Integer siteId,JSONObject json, Integer channelId, 
			Integer modelId) throws GlobalException;
	
	ContentExt initContributeContentExt(ContentExt contentExt, Integer siteId, ContentContributeDto dto, Integer channelId, 
			Integer modelId) throws GlobalException;
}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.ContentCheckDetail;

/**
 * 内容智能审核详情server接口
 * @author: chenming
 * @date:   2019年12月25日 下午2:35:33
 */
public interface ContentCheckDetailService extends IBaseService<ContentCheckDetail, Integer> {
	
	/**
	 * 查询出违禁词JSON，然后过滤掉不在模型中的违禁词JSON
	 * @Title: getCheckBanContent
	 * @param detail	内容智能审核详情
	 * @param items		该内容的模型字段集合
	 * @return: JSONObject
	 */
	JSONObject getCheckBanContent(ContentCheckDetail detail,List<CmsModelItem> items);
	
	/**
	 * 根据内容标识查询出内容智能审核详情
	 * @Title: findByCheckMark
	 * @param checkMark	内容审核标识
	 * @return: ContentCheckDetail
	 */
	ContentCheckDetail findByCheckMark(String checkMark,Integer status);
	
	
	/**
	 * 通过内容id集合查询出内容智能审核集合
	 * @Title: findByContentIds
	 * @param contentIds	内容id集合
	 * @return: List
	 */
	List<ContentCheckDetail> findByContentIds(List<Integer> contentIds);
	
	/**
	 * 查询出审核中的内容智能审核详情集合
	 * @Title: findUnderReviews
	 * @return: List
	 */
	List<ContentCheckDetail> findUnderReviews();
}

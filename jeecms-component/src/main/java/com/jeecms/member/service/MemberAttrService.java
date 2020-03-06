/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.member.domain.MemberAttr;

/**
 * 会员自定义属性Service
* @author ljw
* @version 1.0
* @date 2019-07-10
*/
public interface MemberAttrService extends IBaseService<MemberAttr, Integer> {

	/**
	 * 根据前台传值转化成对应的集合
	* @Title: jsonToMemberAttrs 
	* @param obj 对象
	* @param memberId 会员ID
	* @param items 模型字段集合
	* @return Set
	* @throws GlobalException 异常
	 */
	List<MemberAttr> jsonToMemberAttrs(JSONObject obj,Integer memberId,Set<CmsModelItem> items) 
			throws GlobalException;
	
	/**
	 * 根据memberId删除集合
	* @Title: deleteAttrs 
	* @param memberId 会员ID
	 */
	void deleteAttrs(Integer memberId);
}

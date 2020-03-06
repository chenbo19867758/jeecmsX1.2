/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.member.domain.MemberGroup;

/**   
 * 会员组Dao层
 * @author: wulongwei
 * @date:   2019年4月15日 上午10:02:02     
 */
public interface MemberGroupDao extends IBaseDao<MemberGroup, Integer> {

	/**
	 * 查询是否已经存在默认的会员组信息
	 * @Title: findByIsDefaultAndHasDeleted  
	 * @param isDefault
	 * @param hasDeleted
	 * @return: MemberGroup
	 */
	MemberGroup findByIsDefaultAndHasDeleted(Boolean isDefault, Boolean hasDeleted);
}

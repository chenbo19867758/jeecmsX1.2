/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.member.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.member.dao.ext.MemberLevelDaoExt;
import com.jeecms.member.domain.MemberLevel;


/**
 * 会员等级dao层
 * @author: wulongwei
 * @date:   2019年4月15日 下午4:01:39
 */
public interface MemberLevelDao extends IBaseDao<MemberLevel, Integer>, MemberLevelDaoExt {
	
	/**
	 * 根据等级会员名称查询 等级会员信息
	 * @Title: findByLevelNameAndHasDeleted  
	 * @param levelName 等级名
	 * @param hasDeleted 是否删除
	 * @return: MemberLevel 会员等级
	 */
	MemberLevel findByLevelNameAndHasDeleted(String levelName,Boolean hasDeleted);
	
}

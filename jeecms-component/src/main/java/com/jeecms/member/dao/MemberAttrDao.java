/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.member.domain.MemberAttr;


/**
 * 会员自定义属性Dao
* @author ljw
* @version 1.0
* @date 2019-07-10
*/
public interface MemberAttrDao extends IBaseDao<MemberAttr, Integer> {

	/**
	 * 根据会员id删除集合
	* @Title: findByMemberId 
	* @param memberId 会员ID
	* @return
	 */
	@Modifying
	@Query("delete from MemberAttr bean where bean.memberId=?1")
	void deleteAttrs(Integer memberId);
}

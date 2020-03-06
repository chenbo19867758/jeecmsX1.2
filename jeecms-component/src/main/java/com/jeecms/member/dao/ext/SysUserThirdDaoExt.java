/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao.ext;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.member.domain.SysUserThird;

/**
 * 第三方配置DaoExt
 * @author: ztx
 * @date: 2019年3月1日 下午1:50:00
 */
public interface SysUserThirdDaoExt {

	/**
	 * 查询第三方配置
	* @Title: findByMemberIdAndThirdTypeCode 
	* @param memberId 会员ID
	* @param thirdTypeCode 类型
	* @param hasDeleted 是否删除
	* @throws GlobalException 异常
	 */
	SysUserThird findByMemberIdAndThirdTypeCode(Integer memberId, String thirdTypeCode, Boolean hasDeleted)
			throws GlobalException;

	/**
	 * 根据会员ID，类型查询集合
	* @Title: listByTypeCodeAndMember 
	* @param memberId 会员ID
	* @param thirdTypeCode 类型
	* @param hasDeleted 删除标识
	* @throws GlobalException 异常
	 */
	List<SysUserThird> listByTypeCodeAndMember(Integer memberId, String thirdTypeCode, boolean hasDeleted)
			throws GlobalException;

	/**
	 *  根据第三方ID，类型查询集合
	* @Title: listByThirdIdAndTypeCode 
	* @param thirdId 第三方ID
	* @param typeCode 类型
	* @param hasDeleted 删除标识
	* @throws GlobalException 异常
	 */
	List<SysUserThird> listByThirdIdAndTypeCode(String thirdId, String typeCode, Boolean hasDeleted)
			throws GlobalException;
}

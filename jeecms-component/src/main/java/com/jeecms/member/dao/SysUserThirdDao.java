/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.member.dao.ext.SysUserThirdDaoExt;
import com.jeecms.member.domain.SysUserThird;


/**用户第三方账户Dao
* @author ljw
* @version 1.0
* @date 2019-07-19
*/
public interface SysUserThirdDao extends IBaseDao<SysUserThird, Integer>, SysUserThirdDaoExt {

	/** 根据微信appid查询第三方列表 **/
	@Query("select bean from SysUserThird bean where 1 = 1 and bean.appId=?1 and bean.thirdTypeCode='wechat'")
	@Modifying
	List<SysUserThird> getThirds(String appid);

	/**
	 * 根据微信公众号(appid)，登录方式(typeCode)，用户Id(memberId)查询出会员第三方配置详情
	 * 
	 * @Title: findByThirdTypeCodeAndMemberIdAndAppIdAndHasDeleted
	 * @param typeCode 登录方式(typeCode)
	 * @param memberId 用户Id(memberId)
	 * @param appId 微信公众号(appid)
	 */
	SysUserThird findByThirdTypeCodeAndMemberIdAndAppIdAndHasDeleted(String typeCode, 
			Integer memberId, String appId,
			Boolean hasDeleted);
}

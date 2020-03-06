/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.member.dao.SysUserThirdDao;
import com.jeecms.member.domain.SysUserThird;
import com.jeecms.member.service.SysUserThirdService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.service.BaseServiceImpl;

/**
 * 用户第三方账户绑定表Impl
* @author ljw
* @version 1.0
* @date 2019-07-19
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserThirdServiceImpl extends BaseServiceImpl<SysUserThird,SysUserThirdDao, Integer> 
		implements SysUserThirdService {

	@Override
	public SysUserThird save(SysUserThird bean) throws GlobalException {
		CoreUser member = memberService.findById(bean.getMemberId());
		bean.setMember(member);
		bean = super.save(bean);
		if (member.getMemberThirds() == null) {
			member.setMemberThirds(new ArrayList<>());
		}
		member.getMemberThirds().add(bean);
		return bean;
	}

	@Override
	public SysUserThird deleteInfo(Integer id) throws GlobalException {
		SysUserThird bean = super.delete(id);
		return bean;
	}

	/**
	 * 根据appid查询第三方平台的信息
	 */
	@Override
	public List<SysUserThird> getThirds(String appid) {
		return dao.getThirds(appid);
	}

	@Override
	public List<SysUserThird> findByThirdId(String thirdId, String typeCode) throws GlobalException {
		return dao.listByThirdIdAndTypeCode(thirdId, typeCode, false);
	}

	@Override
	public SysUserThird findByTypeCodeAndMember(String typeCode, Integer memberId) throws GlobalException {
		return dao.findByMemberIdAndThirdTypeCode(memberId, typeCode, false);
	}

	@Override
	public List<SysUserThird> listByTypeCodeAndMember(String typeCode, Integer memberId) throws GlobalException {
		return dao.listByTypeCodeAndMember(memberId, typeCode, false);
	}

	@Override
	public SysUserThird findByTypeCodeAndMemberAndAppId(String typeCode, Integer memberId, String appId)
			throws GlobalException {
		return dao.findByThirdTypeCodeAndMemberIdAndAppIdAndHasDeleted(typeCode, memberId, appId, false);
	}

	@Autowired
	private CoreUserService memberService;
}
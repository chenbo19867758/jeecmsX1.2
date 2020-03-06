package com.jeecms.member.controller.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.common.base.domain.IBaseUser;
import com.jeecms.common.exception.CrossMemberDataExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.util.SystemContextUtils;

/**
 * 会员中心控制器基类
 * @author: tom
 * @date: 2018年8月1日 上午9:26:28
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BaseMemberController<T extends AbstractIdDomain<ID>, ID extends Serializable>
		extends BaseController<T, ID> {

	@Override
	public Map<String, String[]> getCommonParams(HttpServletRequest request) {
		CoreUser member = SystemContextUtils.getUser(request);
		Map<String, String[]> map = super.getCommonParams(request);
		map.put("EQ_memberId_Integer", new String[] { member.getId().toString() });
		return map;
	}

	@Override
	protected ResponseInfo update(T entity, BindingResult result) throws GlobalException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(entity, member.getId());
		return super.update(entity, result);
	}

	@Override
	protected ResponseInfo updateInBatch(List<T> entities, BindingResult result) throws GlobalException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(entities, member.getId());
		return super.updateInBatch(entities, result);
	}

	@Override
	protected ResponseInfo updateAll(T entity, BindingResult result) throws GlobalException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(entity, member.getId());
		return super.updateAll(entity, result);
	}

	@Override
	protected ResponseInfo delete(ID id) throws GlobalException {
		super.validateId(id);
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(id, member.getId());
		return super.delete(id);
	}

	@Override
	protected ResponseInfo delete(T t) throws GlobalException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(t, member.getId());
		return super.delete(t);
	}

	@Override
	protected ResponseInfo deleteBeatch(ID[] ids) throws GlobalException {
		super.validateIds(ids);
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(ids, member.getId());
		return super.deleteBeatch(ids);
	}

	@Override
	public ResponseInfo physicalDelete(ID id) throws GlobalException {
		super.validateId(id);
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(id, member.getId());
		return super.physicalDelete(id);
	}

	@Override
	public ResponseInfo physicalDelete(ID[] ids) throws GlobalException {
		super.validateIds(ids);
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(ids, member.getId());
		return super.physicalDelete(ids);
	}

	@Override
	public ResponseInfo physicalDeleteInBatch(Iterable<T> entities) throws GlobalException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(entities, member.getId());
		return super.physicalDeleteInBatch(entities);
	}

	@Override
	protected ResponseInfo get(ID id) throws GlobalException {
		super.validateId(id);
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkMember(id, member.getId());
		return super.get(id);
	}

	@Override
	protected ResponseInfo findAllById(Iterable<ID> ids) throws GlobalException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		CoreUser member = SystemContextUtils.getUser(request);
		checkIdsMember(ids, member.getId());
		return super.findAllById(ids);
	}

	protected void checkMember(T bean, Integer memberId) throws GlobalException {
		checkMember(bean.getId(), memberId);
	}

	protected void checkMember(Iterable<T> entities, Integer memberId) throws GlobalException {
		for (T bean : entities) {
			checkMember(bean, memberId);
		}
	}

	protected void checkMember(ID[] ids, Integer memberId) throws GlobalException {
		for (ID id : ids) {
			checkMember(id, memberId);
		}
	}

	protected void checkMember(ID id, Integer memberId) throws GlobalException {
		T dbDomain = service.findById(id);
		if (dbDomain instanceof IBaseUser) {
			IBaseUser dbMemberData = (IBaseUser) dbDomain;
			if (!dbMemberData.getUserId().equals(memberId)) {
				throw new GlobalException(new CrossMemberDataExceptionInfo());
			}
		}
	}

	protected void checkIdsMember(Iterable<ID> ids, Integer memberId) throws GlobalException {
		for (ID id : ids) {
			checkMember(id, memberId);
		}
	}
}

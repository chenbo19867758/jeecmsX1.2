package com.jeecms.admin.controller;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.domain.CmsDataPerm.OpeChannelEnum;
import com.jeecms.system.domain.CmsDataPerm.OpeContentEnum;
import com.jeecms.system.domain.CmsDataPerm.OpeSiteEnum;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 管理控制器基类 如果子类重写了增删改或者未使用基类的增删改，则需要主动调用checkSite 检查是否跨站点数据
 * 如果组织、栏目、内容、站点相关功能特定操作 站点维护 需要主动调用 checkSiteDataPerm 检查数据权限 组织维护 需要主动调用
 * checkOrgDataPerm 检查数据权限 栏目维护 需要主动调用 checkChannelDataPerm 检查数据权限 内容维护 需要主动调用
 * checkContentDataPerm 检查数据权限
 * 
 * @author: tom
 * @date: 2019年4月4日 上午8:55:07
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BaseAdminController<T extends AbstractIdDomain<ID>, ID extends Serializable>
		extends BaseController<T, ID> {

	@Override
	public Map<String, String[]> getCommonParams(HttpServletRequest request) {
		CmsSite site = SystemContextUtils.getSite(request);
		Map<String, String[]> map = super.getCommonParams(request);
		map.put("EQ_siteId_Integer", new String[] { site.getId().toString() });
		return map;
	}

	@Override
	protected ResponseInfo update(T entity, BindingResult result) throws GlobalException {
		checkSite(entity);
		return super.update(entity, result);
	}

	@Override
	protected ResponseInfo updateInBatch(List<T> entities, BindingResult result) throws GlobalException {
		checkSite(entities);
		return super.updateInBatch(entities, result);
	}

	@Override
	protected ResponseInfo updateAll(T entity, BindingResult result) throws GlobalException {
		checkSite(entity);
		return super.updateAll(entity, result);
	}

	@Override
	protected ResponseInfo delete(ID id) throws GlobalException {
		super.validateId(id);
		checkSite(id);
		return super.delete(id);
	}

	@Override
	protected ResponseInfo delete(T t) throws GlobalException {
		checkSite(t);
		return super.delete(t);
	}

	@Override
	protected ResponseInfo deleteBeatch(ID[] ids) throws GlobalException {
		super.validateIds(ids);
		checkSite(ids);
		return super.deleteBeatch(ids);
	}

	@Override
	public ResponseInfo physicalDelete(ID id) throws GlobalException {
		super.validateId(id);
		checkSite(id);
		return super.physicalDelete(id);
	}

	@Override
	public ResponseInfo physicalDelete(ID[] ids) throws GlobalException {
		super.validateIds(ids);
		checkSite(ids);
		return super.physicalDelete(ids);
	}

	@Override
	public ResponseInfo physicalDeleteInBatch(Iterable<T> entities) throws GlobalException {
		checkSite(entities);
		return super.physicalDeleteInBatch(entities);
	}

	@Override
	protected ResponseInfo get(ID id) throws GlobalException {
		super.validateId(id);
		checkSite(id);
		return super.get(id);
	}

	@Override
	protected ResponseInfo findAllById(Iterable<ID> ids) throws GlobalException {
		checkIdsSite(ids);
		return super.findAllById(ids);
	}

	protected void checkSite(T bean) throws GlobalException {
		checkSite(bean.getId());
	}

	protected void checkSite(Iterable<T> entities) throws GlobalException {
		for (T bean : entities) {
			checkSite(bean);
		}
	}

	protected void checkSite(ID[] ids) throws GlobalException {
		for (ID id : ids) {
			checkSite(id);
		}
	}

	protected void checkSite(ID id) throws GlobalException {
		service.commonCheck(id);
	}

	/**
	 * 站点数据权限检查
	 * 
	 * @Title: checkSiteDataPerm
	 * @param id
	 *            站点id
	 * @param operator
	 *            站点操作 OpeSiteEnum
	 * @throws GlobalException
	 *             GlobalException
	 */
	protected void checkSiteDataPerm(Integer id, OpeSiteEnum operator) throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
		user.checkSiteDataPerm(id, operator);
	}

	/**
	 * 检查栏目数据权限
	 * 
	 * @Title: checkChannelDataPerm
	 * @param id
	 *            栏目id
	 * @param operator
	 *            栏目操作OpeChannelEnum
	 * @throws GlobalException
	 *             GlobalException
	 */
	protected void checkChannelDataPerm(Integer id, OpeChannelEnum operator) throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
		user.checkChannelDataPerm(id, operator);
	}

	/**
	 * 检查内容数据权限
	 * 
	 * @Title: checkContentDataPerm
	 * @param channelId 栏目id
	 * @param operator
	 *            内容操作 OpeContentEnum
	 * @throws GlobalException
	 *             GlobalException
	 */
	protected void checkContentDataPerm(Integer channelId, OpeContentEnum operator) throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
		user.checkContentDataPerm(channelId, operator);
	}

	protected void checkIdsSite(Iterable<ID> ids) throws GlobalException {
		for (ID id : ids) {
			checkSite(id);
		}
	}
}

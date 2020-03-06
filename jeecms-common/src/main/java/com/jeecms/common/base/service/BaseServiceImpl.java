package com.jeecms.common.base.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.common.base.domain.AbstractDelFlagDomain;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.common.base.domain.AbstractSortDomain;
import com.jeecms.common.base.domain.AbstractTreeDomain;
import com.jeecms.common.base.domain.IBaseSite;
import com.jeecms.common.base.domain.IBaseUser;
import com.jeecms.common.base.domain.Sort;
import com.jeecms.common.exception.CrossSiteDataExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jpa.SearchFilter;
import com.jeecms.common.local.UserThreadLocal;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.web.util.RequestUtils;

/**
 * service 基类实现
 * 
 * @author: tom
 * @date: 2018年4月5日 下午1:57:33
 * @param <T>
 *            Entity类
 * @param <DAO>
 *            DAO接口
 * @param <ID>
 *            ID类型
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Transactional(rollbackFor = Exception.class)
public class BaseServiceImpl<T, DAO extends IBaseDao<T, ID>, ID extends Serializable> implements IBaseService<T, ID> {

	private Map<String, String[]> params = new HashMap<String, String[]>();
	private Paginable paginable = new PaginableRequest(0, Integer.MAX_VALUE);

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Page<T> getPage(Map<String, String[]> params, Pageable pageable, boolean cacheable) {
		return dao.findAll(SearchFilter.spec(params, dao.getDynamicClass(), true), pageable, cacheable);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<T> getList(Map<String, String[]> params, Paginable paginable, boolean cacheable) {
		return dao.findAll(SearchFilter.spec(params, dao.getDynamicClass(), true), paginable, cacheable);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public T findOne(Map<String, String[]> params, boolean cacheable) {
		Optional<T> result = dao.findOne(SearchFilter.spec(params, dao.getDynamicClass(), true), cacheable);
		if (result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<T> findAll(boolean cacheable) {
		return getList(params, paginable, cacheable);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<T> findAllById(Iterable<ID> ids) {
		return dao.findAllById(ids);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public T get(ID id) {
		return dao.getOne(id);
	}

	@Override
	@Transactional(rollbackFor = GlobalException.class, readOnly = true)
	public T findById(ID id) {
		Optional<T> bean = dao.findById(id);
		if (bean.isPresent()) {
			return bean.get();
		} else {
			return null;
		}
	}

	@Override
	public boolean existsById(ID id) {
		return dao.existsById(id);
	}

	@Override
	public long count(@Nullable Specification<T> spec) {
		return dao.count(spec);
	}

	@Override
	public long count(Map<String, String[]> params) {
		return count(SearchFilter.spec(params, dao.getDynamicClass(), true));
	}

	@Override
	public T save(T bean) throws GlobalException {
		bean = dao.save(bean);
		/** 经过 TreeInterceptor TreeDomain类型数据未提交事务 */
		if (bean instanceof AbstractTreeDomain) {
			flush();
		}
		return bean;
	}

	@Override
	public <S extends T> List<S> saveAll(Iterable<S> entities) throws GlobalException {
		return dao.saveAll(entities);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T updateAll(T bean) throws GlobalException {
		commonCheck(bean);
		if (bean instanceof AbstractDomain) {
			AbstractDomain domain = (AbstractDomain) bean;
			AbstractDomain dbDomain = (AbstractDomain) dao.getOne((ID) domain.getId());
			domain.setCreateTime(dbDomain.getCreateTime());
			domain.setCreateUser(dbDomain.getCreateUser());
			domain.setHasDeleted(dbDomain.getHasDeleted());
			bean = (T) domain;
		}
		return dao.save(bean);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T update(T bean) throws GlobalException {
		if (bean instanceof AbstractIdDomain) {
			commonCheck(bean);
			AbstractIdDomain idDomain = (AbstractIdDomain) bean;
			T dbDomain = dao.getOne((ID) idDomain.getId());
			com.jeecms.common.util.MyBeanUtils.copyProperties(bean, dbDomain);
			T updateDomain = dao.save(dbDomain);
			return updateDomain;
		}
		return dao.save(bean);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterable<T> batchUpdate(Iterable<T> entities) throws GlobalException {
		List<T> beans = new ArrayList<T>();
		for (T entity : entities) {
			commonCheck(entity);
			if (entity instanceof AbstractIdDomain) {
				AbstractIdDomain idDomain = (AbstractIdDomain) entity;
				T dbDomain = dao.getOne((ID) idDomain.getId());
				com.jeecms.common.util.MyBeanUtils.copyProperties(entity, dbDomain);
				entity = dbDomain;
			}
			beans.add(entity);
		}
		beans = dao.saveAll(beans);
		flush();
		return beans;
	}

	@Override
	public Iterable<T> batchUpdateAll(Iterable<T> entities) throws GlobalException {
		List<T> beans = new ArrayList<T>();
		for (T entity : entities) {
			commonCheck(entity);
			beans.add(updateAll(entity));
		}
		return beans;
	}

	@Override
	public void flush() {
		dao.flush();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T delete(ID id) throws GlobalException {
		T bean = dao.getOne(id);
		commonCheck(bean);
		if (bean instanceof AbstractDelFlagDomain) {
			AbstractDelFlagDomain delDomain = (AbstractDelFlagDomain) bean;
			delDomain.setHasDeleted(true);
			update((T) delDomain);
		}
		return bean;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T delete(T bean) throws GlobalException {
		commonCheck(bean);
		if (bean instanceof AbstractDelFlagDomain) {
			AbstractDelFlagDomain delDomain = (AbstractDelFlagDomain) bean;
			delDomain.setHasDeleted(true);
			update((T) delDomain);
		}
		return bean;
	}

	@Override
	public List<T> delete(ID[] ids) throws GlobalException {
		List<T> beans = new ArrayList<T>(ids.length);
		for (ID id : ids) {
			commonCheck(id);
			beans.add(delete(id));
		}
		return beans;
	}

	@Override
	public Iterable<T> delete(Iterable<T> entities) throws GlobalException {
		List<T> beans = new ArrayList<T>();
		for (T entity : entities) {
			commonCheck(entity);
			beans.add(delete(entity));
		}
		return beans;
	}

	@Override
	public T physicalDelete(ID id) throws GlobalException {
		T bean = dao.getOne(id);
		commonCheck(bean);
		dao.deleteById(id);
		return bean;
	}

	@Override
	public T physicalDelete(T entity) throws GlobalException {
		commonCheck(entity);
		dao.delete(entity);
		return entity;
	}

	@Override
	public List<T> physicalDelete(ID[] ids) throws GlobalException {
		List<T> beans = new ArrayList<T>(ids.length);
		for (ID id : ids) {
			commonCheck(id);
			beans.add(physicalDelete(id));
		}
		return beans;
	}

	@Override
	public void physicalDeleteInBatch(Iterable<T> entities) throws GlobalException {
		for (T en : entities) {
			commonCheck(en);
		}
		dao.deleteInBatch(entities);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> sort(Sort[] sorts) throws GlobalException {
		List<T> lists = new ArrayList<T>();
		for (Sort sort : sorts) {
			AbstractSortDomain<ID> entity = (AbstractSortDomain) get((ID) sort.getId());
			entity.setSortNum(sort.getSortNum());
			commonCheck((ID) sort.getId());
			lists.add(update((T) entity));
		}
		return lists;
	}

	@Override
	public void commonCheck(T entity) throws GlobalException {
		/**
		IBaseUser user = UserThreadLocal.getUser();
		if (RequestUtils.isAdminRequest()) {
			if (entity instanceof IBaseSite) {
				IBaseSite dbSiteDomain = (IBaseSite) entity;
				if (user!=null&&!user.getOwnerSiteIds().contains(dbSiteDomain.getSiteId())) {
					throw new GlobalException(new CrossSiteDataExceptionInfo());
				}
			}
		}
		*/
	}

	/**
	 * 检查 后台管理数据是否在用户的站群权限内 会员中心的则检查是否检查是否用户自己的数据
	 * 
	 * @Title: commonCheck
	 * @param id
	 *            ID
	 * @throws GlobalException
	 *             GlobalException
	 */
	@Override
	public void commonCheck(ID id) throws GlobalException {
		T dbDomain = findById(id);
		commonCheck(dbDomain);
	}

	@Autowired
	protected DAO dao;
	protected Class<T> entityClass;

}

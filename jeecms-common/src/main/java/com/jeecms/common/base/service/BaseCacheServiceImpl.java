package com.jeecms.common.base.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.common.exception.GlobalException;

/**
 * 实现了缓存的service
 * 
 * @author: tom
 * @date: 2018年5月23日 下午2:23:23
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Transactional(rollbackFor = Exception.class)
@CacheConfig(cacheNames = "BaseService-")
public class BaseCacheServiceImpl<T, DAO extends IBaseDao<T, ID>, ID extends Serializable>
		extends BaseServiceImpl<T, DAO, ID> {

	@Override
	@Cacheable
	public T get(ID id){
		return super.get(id);
	}

	@Override
	@Cacheable
	public T findById(ID id)  {
		return super.findById(id);
	}

	@Override
	@CachePut
	public T save(T bean) throws GlobalException {
		return super.save(bean);
	}

	@Override
	@CachePut
	public T updateAll(T bean)throws GlobalException {
		return super.updateAll(bean);
	}

	@Override
	@CachePut
	public T update(T bean)throws GlobalException {
		return super.update(bean);
	}

	@Override
	@CachePut
	public T delete(ID id) throws GlobalException {
		return super.delete(id);
	}

	@Override
	@CachePut
	public List<T> delete(ID[] ids) throws GlobalException {
		return super.delete(ids);
	}

	@Override
	@CacheEvict(beforeInvocation = true)
	public T physicalDelete(ID id) throws GlobalException {
		return super.physicalDelete(id);
	}

	@Override
	@CacheEvict(beforeInvocation = true, allEntries = true)
	public List<T> physicalDelete(ID[] ids) throws GlobalException {
		return super.physicalDelete(ids);
	}

	@Override
	@CacheEvict(beforeInvocation = true, allEntries = true)
	public void physicalDeleteInBatch(Iterable<T> entities) throws GlobalException {
		super.physicalDeleteInBatch(entities);
	}

}

package com.jeecms.common.base.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import com.jeecms.common.base.domain.Sort;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;

/**
 * service基类接口
 * 
 * @author: tom
 * @date: 2018年4月5日 下午1:48:04
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface IBaseService<T, ID extends Serializable> {

	/**
	 * 分页通用查询
	 * 
	 * @Title: getPage
	 * @param cacheable 是否使用查询缓存
	 * @param params 通用参数
	 * @param pageable 分页对象
	 * @return: Page 分页对象
	 */
	public Page<T> getPage(Map<String, String[]> params, Pageable pageable, boolean cacheable);

	/**
	 * 通用查询列表，支持从第几条数据后抓取指定条数
	 * 
	 * @Title: getList
	 * @param cacheable 是否使用缓存
	 * @param params 通用查询参数
	 * @param paginable Paginable
	 * @return: List
	 */
	public List<T> getList(Map<String, String[]> params, Paginable paginable, boolean cacheable);
	
	/**
	 * 条件查询单个对象
	 * 
	 * @Title: findOne
	 * @param params 查询参数
	 * @param cacheable TODO
	 * @return: T
	 */
	public T findOne(Map<String, String[]> params, boolean cacheable);

	/**
	 * 列表查询所有
	 * 
	 * @Title: findAll
	 * @param cacheable 是否使用查询缓存
	 * @return: List
	 */
	public List<T> findAll(boolean cacheable);

	/**
	 * get方式获取单个对象，建议使用findById方法 未找到抛出Null异常
	 * 
	 * @Title: get
	 * @param id 查询ID
	 * @return: T
	 */
	public T get(ID id);

	/**
	 * findById获取单个对象，未找到记录则抛出未找到对象
	 * 
	 * @Title: findById
	 * @param id 查询ID
	 * @return: T
	 */
	public T findById(ID id);

	/**
	 * 批量根据ID查询列表
	 * 
	 * @Title: findAllById
	 * @param ids id集合
	 * @return: List
	 */
	public List<T> findAllById(Iterable<ID> ids);

	/**
	 * 判断是否存在id的对象
	 * 
	 * @Title: existsById
	 * @param id  查询ID
	 * @return: boolean
	 */
	public boolean existsById(ID id);

	/**
	 * 按照查询条件计算条数
	 * 
	 * @Title: count
	 * @param spec 查询参数 Specification
	 * @return: long
	 */
	public long count(@Nullable Specification<T> spec);

	/**
	 * 封装的查询条件计算条数
	 * 
	 * @Title: count
	 * @param params 查询参数
	 * @return: long
	 */
	public long count(Map<String, String[]> params);

	/**
	 * 批量保存
	 * 
	 * @Title: saveAll
	 * @param entities 批量保存对象
	 * @return: List
	 * @throws GlobalException GlobalException
	 */
	public <S extends T> List<S> saveAll(Iterable<S> entities) throws GlobalException;

	/**
	 * 保存
	 * 
	 * @Title: save
	 * @param bean 保存对象
	 * @return: T
	 * @throws GlobalException GlobalException
	 */
	public T save(T bean) throws GlobalException;

	/**
	 * 修改对象，为null也修改同步
	 * 
	 * @Title: updateAll
	 * @param bean 修改对象
	 * @throws GlobalException GlobalException
	 * @return: T
	 */
	public T updateAll(T bean)throws GlobalException;

	/**
	 * 只修改不为空的数据
	 * 
	 * @Title: update
	 * @param bean 修改对象
	 * @throws GlobalException GlobalException
	 * @return: T
	 */
	public T update(T bean)throws GlobalException;

	/**
	 * 批量修改不为空的数据
	 * 
	 * @Title: batchUpdate
	 * @param entities
	 *            批量修改的对象
	 * @throws GlobalException GlobalException
	 * @return: Iterable
	 */
	public Iterable<T> batchUpdate(Iterable<T> entities)throws GlobalException;

	/**
	 * 批量修改
	 * 
	 * @Title: batchUpdateAll
	 * @param entities
	 *            批量修改的对象
	 * @throws GlobalException GlobalException
	 * @return: Iterable
	 */
	public Iterable<T> batchUpdateAll(Iterable<T> entities)throws GlobalException;

	/**
	 * 刷新
	 * 
	 * @Title: flush
	 */
	public void flush();

	/**
	 * 逻辑删除
	 * 
	 * @Title: delete
	 * @param id id
	 * @return: T
	 * @throws GlobalException GlobalException
	 */
	public T delete(ID id) throws GlobalException;

	/**
	 * 根据id数组批量逻辑删除
	 * 
	 * @Title: delete
	 * @param ids 逻辑删除 ID数组
	 * @return: List
	 * @throws GlobalException GlobalException
	 */
	public List<T> delete(ID[] ids) throws GlobalException;

	/**
	 * 逻辑删除
	 * 
	 * @Title: delete
	 * @param bean 逻辑删对象
	 * @return: T
	 * @throws GlobalException GlobalException
	 */
	public T delete(T bean) throws GlobalException;

	/**
	 * 批量逻辑删除
	 * 
	 * @Title: delete
	 * @param entities 批量删除的对象
	 * @return: Iterable
	 * @throws GlobalException GlobalException
	 */
	public Iterable<T> delete(Iterable<T> entities) throws GlobalException;

	/**
	 * 物理删除
	 * 
	 * @Title: physicalDelete
	 * @param id 物理删除对象ID
	 * @return: T
	 * @throws GlobalException GlobalException
	 */
	public T physicalDelete(ID id) throws GlobalException;

	/**
	 * 物理删除
	 * 
	 * @Title: physicalDelete
	 * @param entity 物理删掉的对象
	 * @return: T
	 * @throws GlobalException GlobalException
	 */
	public T physicalDelete(T entity) throws GlobalException;

	/**
	 * 根据id数组物理删除
	 * 
	 * @Title: physicalDelete
	 * @param ids
	 *            物理删除对象的ID数组
	 * @return: List
	 * @throws GlobalException GlobalException
	 */
	public List<T> physicalDelete(ID[] ids) throws GlobalException;

	/**
	 * 根据对象集合物理删除
	 * @Title: physicalDeleteInBatch
	 * @param entities 物理删除对象集合
	 * @return: void
	 * @throws GlobalException GlobalException
	 */
	public void physicalDeleteInBatch(Iterable<T> entities) throws GlobalException;

	/**
	 * 批量保存排序， 修改的目标实体必须继承AbstractSortDomain基类
	 * 
	 * @Title: sort
	 * @param sorts Sort 排序对象集合
	 * @throws GlobalException GlobalException
	 * @return: List
	 */
	List<T> sort(Sort[] sorts)throws GlobalException;
	
	/**
	 * 检查站点、用户
	 * @Title: commonCheck
	 * @param entity G
	 * @throws GlobalException GlobalException
	 */
	void commonCheck(T entity) throws GlobalException;
	
	/**
	 * 检查站点、用户
	 * @Title: commonCheck
	 * @param id  id
	 * @throws GlobalException GlobalException
	 */
	void commonCheck(ID id) throws GlobalException;

}

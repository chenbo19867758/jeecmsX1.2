package com.jeecms.common.web.cache;

import java.io.Serializable;

/**
 * cache提供者
 * @author: tom
 * @date: 2018年9月27日 上午10:18:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CacheProvider {

	/**
	 * 获取缓存对象
	 * 
	 * @Title: getCache
	 * @param cacheName
	 *            缓存区块名
	 * @param id
	 *            缓存key
	 * @return: Serializable
	 */
	public Serializable getCache(String cacheName, String id);

	/**
	 * 存对象入缓存
	 * 
	 * @Title: setCache
	 * @param cacheName
	 *            缓存区块名
	 * @param id
	 *            缓存key
	 * @param value
	 *            对象
	 * @return: void
	 */
	public void setCache(String cacheName, String id, Serializable value);

	/**
	 * 删除缓存
	 * 
	 * @Title: clearCache
	 * @param cacheName
	 *            缓存区块名
	 * @param id
	 *            缓存key
	 * @return: void
	 */
	public void clearCache(String cacheName, String id);

	/**
	 * 删除缓存区块下所有缓存
	 * 
	 * @Title: clearAll
	 * @param cacheName
	 *            缓存区块名
	 * @return: void
	 */
	public void clearAll(String cacheName);

	/**
	 * 判断缓存是否存在某key的缓存
	 * 
	 * @Title: exist
	 * @param cacheName 缓存区块名
	 * @param id 缓存key
	 * @return: boolean
	 */
	public boolean exist(String cacheName, String id);

}

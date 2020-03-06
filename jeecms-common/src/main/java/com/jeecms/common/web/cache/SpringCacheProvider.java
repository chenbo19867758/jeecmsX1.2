package com.jeecms.common.web.cache;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 默认使用ehcache 有redis则是redis
 * @author: tom
 * @date: 2018年9月27日 下午5:10:58
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class SpringCacheProvider implements CacheProvider {

	@Override
	public Serializable getCache(String cacheName, String id) {
		Cache cache = getCache(cacheName);
		if (cache == null) {
			return null;
		}
		ValueWrapper e = cache.get(id);
		if (e != null) {
			return (Serializable) e.get();
		} else {
			return null;
		}
	}

	private Cache getCache(String cacheName) {
		// 获取Cache
		Cache cache = cacheManager.getCache(cacheName);
		return cache;
	}
	
	@Override
	public void setCache(String cacheName, String id, Serializable session) {
		Cache cache = getCache(cacheName);
		cache.put(id, session);
	}

	@Override
	public void clearCache(String cacheName, String id) {
		Cache cache = getCache(cacheName);
		cache.evict(id);
	}

	@Override
	public void clearAll(String cacheName) {
		Cache cache = getCache(cacheName);
		cache.clear();
	}

	@Override
	public boolean exist(String cacheName, String id) {
		Cache cache = getCache(cacheName);
		ValueWrapper e = cache.get(id);
		if (e != null && e.get() != null) {
			return true;
		} else {
			return false;
		}
	}

	@Autowired
	CacheManager cacheManager;
}

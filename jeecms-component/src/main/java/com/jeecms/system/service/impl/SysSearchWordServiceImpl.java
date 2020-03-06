/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.common.util.StrUtils;
import com.jeecms.system.dao.SearchWordDao;
import com.jeecms.system.domain.SysSearchWord;
import com.jeecms.system.domain.dto.SearchWordDto;
import com.jeecms.system.service.SysSearchWordService;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 搜索词Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysSearchWordServiceImpl extends BaseServiceImpl<SysSearchWord, SearchWordDao, Integer> implements SysSearchWordService, DisposableBean {

	private Logger logger = LoggerFactory.getLogger(SysSearchWordServiceImpl.class);

	@Override
	public List<SysSearchWord> saveBatch(SysSearchWord sysSearchWord, Integer siteId) throws GlobalException {
		String word = sysSearchWord.getWord();
		String[] words = StrUtils.splitAndTrim(word, ",", "，");
		Set<String> set = new HashSet<String>(Arrays.asList(words));
		List<SysSearchWord> list = new ArrayList<SysSearchWord>(words.length);
		for (String str : set) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			//判断是否有该搜索词，有则跳过不添加
			if (checkWord(str, null, siteId)) {
				SysSearchWord searchWord = new SysSearchWord();
				searchWord.setWord(str);
				//获取汉字的首字母
				String letter = ChineseCharToEn.getAllFirstLetter(str);
				searchWord.setIniChinese(letter);
				searchWord.setIsRecommend(sysSearchWord.getIsRecommend());
				searchWord.setSiteId(siteId);
				searchWord.setSortNum(sysSearchWord.getSortNum());
				//添加搜索词时默认为0
				searchWord.setSearchCount(0);
				list.add(searchWord);
			}
		}
		return saveAll(list);
	}

	@Override
	public boolean checkWord(String word, Integer id, Integer siteId) {
		//word为空直接返回true
		if (StringUtils.isBlank(word)) {
			return true;
		}
		SysSearchWord searchWord = dao.findByWord(word, siteId);
		//searchWord为空直接返回true
		if (searchWord == null) {
			return true;
		} else {
			if (!searchWord.getId().equals(id)) {
				//如果id为空说明word存在，返回false；或者id存在，但与searchWord的id不相等,说明word存在， 返回false
				return false;
			}
		}
		return true;
	}

	@Override
	public void saveSearchWord(Integer siteId, String word) throws GlobalException {
		if (StringUtils.isNotBlank(word)) {
			word = word.trim();
		}
		if (StringUtils.isNotBlank(word)) {
			Element e = cache.get(word);
			Integer searchCount = 1;
			SearchWordDto wordCache;
			if (e != null) {
				wordCache = (SearchWordDto) e.getObjectValue();
				searchCount = wordCache.getSearchCount() + 1;
			} else {
				searchCount = 1;
				wordCache = new SearchWordDto(word, siteId, searchCount);
			}
			wordCache.setSearchCount(searchCount);
			try {
				cache.put(new Element(word, wordCache));
			} catch (Exception e1) {
				logger.error(e1.getMessage());
			}
			refreshToDB();
		}
	}

	@Override
	public List<SysSearchWord> getList(Integer siteId, int orderBy, Paginable paginable) {
		return dao.getList(siteId, orderBy, paginable);
	}

	@Override
	public void destroy() throws Exception {
		refreshToDB();
	}

	/**
	 * SpringCache 接口暂无获取所有key,还是采用Ehcache缓存数据
	 *
	 * @throws GlobalException GlobalException
	 * @Title: refreshToDB
	 * @return: void
	 */
	private synchronized void refreshToDB() throws GlobalException {
		long time = System.currentTimeMillis();
		if (time > refreshTime + interval) {
			synchronized (String.valueOf(time)) {
				refreshTime = time;
				saveToDB(cache);
				// 清除缓存
				cache.removeAll();
			}

		}
	}

	@SuppressWarnings("unchecked")
	private void saveToDB(Ehcache cache) throws GlobalException {
		List<String> keys = cache.getKeys();
		if (keys.size() <= 0) {
			return;
		}
		Element e;
		SearchWordDto wordCache;
		for (String key : keys) {
			e = cache.get(key);
			if (e != null) {
				wordCache = (SearchWordDto) e.getObjectValue();
				if (wordCache != null) {
					Paginable paginable = new PaginableRequest(0, 1);
					SysSearchWord w;
					List<SysSearchWord> words = super.dao.getList(wordCache.getSiteId(), key, null,
							SysSearchWord.ODER_BY_SEARCH_COUNT_DESC, paginable);
					// 已有则更新搜索次数
					if (words != null && words.size() > 0) {
						w = words.get(0);
						w.setSearchCount(w.getSearchCount() + wordCache.getSearchCount());
						super.update(w);
					} else {
						if(wordCache.getSiteId()!=null){
							w = new SysSearchWord();
							w.setSearchCount(0);
							w.setIsRecommend(false);
							w.setSortNum(SysSearchWord.SORT_DEF);
							w.setIniChinese(ChineseCharToEn.getAllFirstLetter(key));
							w.setWord(key);
							w.setSearchCount(wordCache.getSearchCount());
							w.setSiteId(wordCache.getSiteId());
							super.save(w);
						}
					}
				}
			}
		}

	}

	/**
	 * 间隔时间 5分钟
	 */
	private int interval = 1 * 60 * 1000;
	/**
	 * 最后刷新时间
	 */
	private long refreshTime = System.currentTimeMillis();
	private Ehcache cache;

	@Autowired
	public void setCache(@Qualifier("searchWordCache") Ehcache cache) {
		this.cache = cache;
	}

}
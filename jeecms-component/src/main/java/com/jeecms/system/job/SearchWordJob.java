package com.jeecms.system.job;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.system.domain.SysSearchWord;
import com.jeecms.system.service.SysSearchWordService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *搜索词检查操作，维护热搜词商品数并且删除非热词
 * @author: tom
 * @date: 2018年6月11日 下午6:34:21
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SearchWordJob implements IBaseJob {
	private Logger logger = LoggerFactory.getLogger(SearchWordJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
		JobDataMap map = context.getMergedJobDataMap();
		logger.info("Running Job name : {} ", map.getString("name"));
		long startTime = System.currentTimeMillis();
		initService();
		try {
			Map<String, String[]> params = new HashMap<String, String[]>(0);
			if (getTotalPage() == null) {
				long totalCount = searchWordService.count(params);
				setTotalPage((totalCount / oneTimeQueryCount + 1));
			}
			WordThread thread = new WordThread(params);
			thread.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		logger.info(">>>>>>>>>>>>> Running Job has been completed , cost time :  " 
		                + (endTime - startTime) + "ms\n");
	}

	private class WordThread extends Thread {
		Map<String, String[]> params;

		public WordThread(Map<String, String[]> params) {
			super();
			this.params = params;
		}

		@Override
		public void run() {
			Boolean result = false;
			while (!result) {
				try {
					Thread.sleep(threadSleepSecond * 1000);
					Paginable paginable = new PaginableRequest(
					                getLastQueryCount(), oneTimeQueryCount);
					setLastQueryCount(oneTimeQueryCount * (getQueryPage() + 1));
					setQueryPage(getQueryPage() + 1);
					List<SysSearchWord> words = searchWordService.getList(params, paginable, false);
					for (SysSearchWord w : words) {
						/**
						 * 小于clearSearchCount次数的执行清理
						 */
						if (w.getSearchCount() < clearSearchCount) {
							searchWordIdList.add(w.getId());
						} else {
							/**
							 * 大于clearProductCount次的维护商品数量
							 */
							// Long hitProductNum =
							// productLuceneService.searchCount(w.getWord());
							// TODO 查询索引
							Long hitProductNum = 0L;
							if (hitProductNum < clearProductCount) {
								/**
								 * 搜索词商品数太少的也清除
								 */
								searchWordIdList.add(w.getId());
							} else {
								//w.setDocNum(hitProductNum.intValue());
								searchWordService.update(w);
							}
						}
					}
					/**
					 * 执行完成
					 */
					if (words.size() <= 0 | getQueryPage() > getTotalPage()) {
						try {
							for (Integer id : searchWordIdList) {
								searchWordService.physicalDelete(id);
							}
						} catch (GlobalException e) {
							logger.error(e.getMessage());
						}
						result = true;
						break;
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}

		}

	}

	private void initService() {
		searchWordService = ApplicationContextProvider.getBean(SysSearchWordService.class);
	}

	private SysSearchWordService searchWordService;

	/**
	 * 词汇搜索次数
	 */
	@Value("${product.searchWord.clearSearchCount}")
	private Integer clearSearchCount = 10;
	/**
	 * 词汇商品数
	 */
	@Value("${product.searchWord.clearProductCount}")
	private Integer clearProductCount = 10;
	/**
	 * 线程间隔时间执行秒数(默认30秒)
	 */
	private Integer threadSleepSecond = 30;
	/**
	 * 搜索词总页数
	 */
	private Long totalPage;
	/**
	 * 一次处理多少条，分批次处理
	 */
	private int oneTimeQueryCount = 1000;
	/**
	 * 最后处理条数
	 */
	private Integer lastQueryCount = 0;
	/**
	 * 查询了第几页
	 */
	private Integer queryPage = 0;

	private Set<Integer> searchWordIdList = new HashSet<Integer>();

	public Long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getQueryPage() {
		return queryPage;
	}

	public void setQueryPage(Integer queryPage) {
		this.queryPage = queryPage;
	}

	public Integer getLastQueryCount() {
		return lastQueryCount;
	}

	public void setLastQueryCount(Integer lastQueryCount) {
		this.lastQueryCount = lastQueryCount;
	}

}

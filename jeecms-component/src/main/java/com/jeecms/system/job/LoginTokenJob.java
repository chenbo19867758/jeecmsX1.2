package com.jeecms.system.job;

import com.jeecms.auth.domain.LoginToken;
import com.jeecms.auth.service.LoginTokenService;
import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.web.ApplicationContextProvider;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 删除过期的token
 * @author: tom
 * @date: 2018年7月19日 下午6:22:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class LoginTokenJob implements IBaseJob {
	private Logger logger = LoggerFactory.getLogger(LoginTokenJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
		JobDataMap map = context.getMergedJobDataMap();
		logger.info("Running Job name : {} ", map.getString("name"));
		long startTime = System.currentTimeMillis();
		try {
			initService();
			if (getTotalPage() == null) {
				long totalCount = loginTokenService.getExpireTokenCount();
				setTotalPage((totalCount / oneTimeQueryCount + 1));
			}
			TokenDeleteThread thread = new TokenDeleteThread();
			thread.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		logger.info(">>>>>>>>>>>>> Running Job has been completed , cost time :  " 
		                + (endTime - startTime) + "ms\n");
	}

	private class TokenDeleteThread extends Thread {

		public TokenDeleteThread() {
			super();
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
					List<LoginToken> tokens = loginTokenService.getExpireTokenList(paginable);
					for (LoginToken w : tokens) {
						tokenIdList.add(w.getId());
					}
					/**
					 * 执行完成
					 */
					if (tokens.size() <= 0 | getQueryPage() > getTotalPage()) {
						for (Integer id : tokenIdList) {
							loginTokenService.physicalDelete(id);
						}
						result = true;
						break;
					}
				} catch (InterruptedException | GlobalException e) {
					logger.error(e.getMessage());
				}
			}

		}

	}

	private void initService() {
		loginTokenService = ApplicationContextProvider.getBean(LoginTokenService.class);
	}

	private LoginTokenService loginTokenService;
	/**
	 * 线程间隔时间执行秒数(默认30秒)
	 */
	private Integer threadSleepSecond = 10;
	/**
	 * 总页数
	 */
	private Long totalPage;
	/**
	 * 一次处理多少条，分批次处理
	 */
	private int oneTimeQueryCount = 1000;
	/**
	 * 最后处理页数
	 */
	private Integer lastQueryPage = 0;
	/**
	 * 最后处理条数
	 */
	private Integer lastQueryCount = 0;
	/**
	 * 查询了第几页
	 */
	private Integer queryPage = 0;

	private Set<Integer> tokenIdList = new HashSet<Integer>();

	public Long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getLastQueryPage() {
		return lastQueryPage;
	}

	public Integer getQueryPage() {
		return queryPage;
	}

	public void setLastQueryPage(Integer lastQueryPage) {
		this.lastQueryPage = lastQueryPage;
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

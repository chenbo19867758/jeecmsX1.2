package com.jeecms.system.job;

/**
 * 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.userstatistics.UserStatistics;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.AbstractWeChatToken;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.AbstractWeChatTokenService;
import com.jeecms.wechat.service.WechatFansStatisticsService;

/**
 * 保存微信粉丝统计数据
 * @author: wulongwei
 * @date: 2018年8月4日 下午5:34:21
 */
public class UserSummaryJob implements IBaseJob {
	private Logger logger = LoggerFactory.getLogger(UserSummaryJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
		JobDataMap map = context.getMergedJobDataMap();
		logger.info("Running Job name : {} ", map.getString("name"));
		long startTime = System.currentTimeMillis();
		try {
			initService();
			// 因为只能查询到前一天的数据，所以，开始日期、结束日期都是同一天
			UserStatistics statistics = new UserStatistics();
			statistics.setBeginDate(MyDateUtils.formatDate(MyDateUtils
					.getSpecficDateStart(new Date(), -1)));// 开始的日期
			statistics.setEndDate(MyDateUtils.formatDate(MyDateUtils
					.getSpecficDateStart(new Date(), -1))); // 结束的日期
			// 查询weChatInfo和toKen，遍历比较appId 获取到token调用方法
			List<AbstractWeChatInfo> infos = abstractWeChatInfoService.findAll(false);
			List<AbstractWeChatToken> chatTokens = abstractWeChatTokenService.findAll(false);
			Map<String, AbstractWeChatToken> tokenMaps = new HashMap<String, AbstractWeChatToken>();
			if (chatTokens != null && chatTokens.size() > 0) {
				// 将所有公众号token数据List 转换成以appId为key，对象为value的Map
				tokenMaps = chatTokens.stream()
						.collect(Collectors
								.toMap(AbstractWeChatToken::getAppId, 
										a -> a, (k1, k2) -> k2));
			}
			for (AbstractWeChatInfo abstractWeChatInfo : infos) {
				AbstractWeChatToken token = tokenMaps.get(abstractWeChatInfo.getAppId());
				if (token != null) {
					ValidateToken validatoken = new ValidateToken();
					validatoken.setAppId(token.getAppId());
					validatoken.setAccessToken(token.getAuthorizerAccessToken());
					statisticsService.saveFansStatistics(statistics, validatoken);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		logger.info(">>>>>>>>>>>>> 微信粉丝统计数据  Running Job has been completed , cost time :  {} ms\n",
				(endTime - startTime));
	}

	private void initService() {
		statisticsService = ApplicationContextProvider.getBean(WechatFansStatisticsService.class);
		abstractWeChatInfoService = ApplicationContextProvider.getBean(AbstractWeChatInfoService.class);
		abstractWeChatTokenService = ApplicationContextProvider.getBean(AbstractWeChatTokenService.class);
	}

	private WechatFansStatisticsService statisticsService;
	private AbstractWeChatInfoService abstractWeChatInfoService;
	private AbstractWeChatTokenService abstractWeChatTokenService;
}

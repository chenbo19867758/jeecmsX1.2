/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.domain.WechatSend;
import com.jeecms.wechat.service.WechatSendService;

/**
 * 定时群发job
 * @author: ljw
 * @date: 2019年6月10日 下午6:34:21
 */
public class MassManageJob implements IBaseJob {

	private Logger logger = LoggerFactory.getLogger(MassManageJob.class);

	private WechatSendService wechatSendService;

	public MassManageJob() {
	}

	private void initService() {
		wechatSendService = ApplicationContextProvider.getBean(WechatSendService.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
		// JobDataMap map = context.getMergedJobDataMap();
		initService();
		JobDataMap map = context.getJobDetail().getJobDataMap();
		String id = map.get("params").toString();
		WechatSend wechatSend = new WechatSend();
		try {
			wechatSend.setType(WechatConstants.SEND_TYPE_TIME);
			wechatSend = wechatSendService.findById(Integer.valueOf(id));
			wechatSendService.send(wechatSend);
		} catch (Exception e1) {
			logger.info("{}", e1);
			e1.printStackTrace();
		}
	}
}

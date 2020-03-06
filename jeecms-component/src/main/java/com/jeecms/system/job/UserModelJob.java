/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.system.job;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jeecms.auth.service.UserModelRecordService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;

/**   
 * 用户模型统计使用定时JOB
 * @author: ljw
 * @date:   2019年12月14日 上午9:41:41     
 */
@Component
public class UserModelJob {

	@Autowired
	private UserModelRecordService userModelRecordService;
	
	/**
	 * 每一天定时统计用户使用模型的频率 这里不使用Quartz,使用Springboot自带调度器
	 * 
	 * @Title: collect
	 * @Description: 测试每5分钟统计一次,实际可调 0 0/5 * * * ? 实际每天凌晨1点执行0 0 1 * * ?
	 * @throws GlobalException 异常
	 * @since 1.2
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void collect() throws GlobalException {
		userModelRecordService.collect(MyDateUtils.getSpecficDate(new Date(),-1), 
				MyDateUtils.getSpecficDate(new Date(),-1));
	}
}

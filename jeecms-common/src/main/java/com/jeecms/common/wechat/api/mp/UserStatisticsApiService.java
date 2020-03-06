package com.jeecms.common.wechat.api.mp;
/**
 * 用户数据统计
 * <p>Title:UserStatistics</p>
 * @author wulongwei
 * @date 2018年8月2日
 */

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.userstatistics.UserStatistics;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserCumulateResponse;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserSummaryResponse;

public interface UserStatisticsApiService {

	/**
	 * 获取用户增减数据  最大时间跨度7
	 * @Title: getUserSummary  
	 * @param statistics
	 * @param validToken
	 * @return
	 * @throws GlobalException      
	 * @return: getusersummaryResponse
	 */
	UserSummaryResponse getUserSummary(UserStatistics statistics,ValidateToken validToken)throws GlobalException;

	/**
	 * 获取累计用户数据  最大时间跨度7
	 * @Title: getUserCumulate  
	 * @param statistics
	 * @param validToken
	 * @return
	 * @throws GlobalException      
	 * @return: getusercumulateResponse
	 */
	UserCumulateResponse getUserCumulate(UserStatistics statistics,ValidateToken validToken)throws GlobalException;
}

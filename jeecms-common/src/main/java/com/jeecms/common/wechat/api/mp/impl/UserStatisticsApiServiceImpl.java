package com.jeecms.common.wechat.api.mp.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.UserStatisticsApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.userstatistics.UserStatistics;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserCumulateResponse;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserSummaryResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 用户数据统计
 * <p>Title:UserStatisticsApiServiceImpl</p>
 * @author wulongwei
 * @date 2018年8月2日
 */
@Service
public class UserStatisticsApiServiceImpl implements UserStatisticsApiService{

	/**获取用户增减数据*/
    public final String API_USER_SUMMARY=Const.DoMain.API_URI.concat("/datacube/getusersummary");
    /**获取累计用户数据*/
    public final String API_USER_CUMULATE=Const.DoMain.API_URI.concat("/datacube/getusercumulate");

    public final String ACCESS_TOKEN="access_token";
	/**
	 * 获取用户增减数据  最大时间跨度7
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public UserSummaryResponse getUserSummary(UserStatistics statistics,ValidateToken validToken)throws GlobalException {
		Map<String, String> params=new HashMap<String, String>(20);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		UserSummaryResponse response=HttpUtil.postJsonBean(API_USER_SUMMARY, params, SerializeUtil.beanToJson(statistics), UserSummaryResponse.class);
		return response;
	}

	/**
	 * 获取累计用户数据  最大时间跨度7
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public UserCumulateResponse getUserCumulate(UserStatistics statistics,ValidateToken validToken)throws GlobalException {
		Map<String, String> params=new HashMap<String, String>(20);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		
		UserCumulateResponse response=HttpUtil.postJsonBean(API_USER_CUMULATE, params, SerializeUtil.beanToJson(statistics), UserCumulateResponse.class);
		return response;
	}

	
	

}

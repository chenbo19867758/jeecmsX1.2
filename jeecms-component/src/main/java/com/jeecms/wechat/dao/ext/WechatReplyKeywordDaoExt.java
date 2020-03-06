package com.jeecms.wechat.dao.ext;


import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.WechatReplyKeyword;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WechatReplyKeywordDaoExt {
	
	/**
	 * 获取微信密钥
	 * @Title: getKeyword  
	 * @param appId
	 * @param content 内容
	 * @return
	 * @throws GlobalException 全局异常     
	 * @return: WechatReplyKeyword
	 */
	WechatReplyKeyword getKeyword(String appId,String content)throws GlobalException;
}

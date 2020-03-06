/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.voice.service;

import com.jeecms.common.exception.GlobalException;

/**   
 * 百度语音合成Service
 * @author: ljw
 * @date:   2019年9月11日 下午1:54:32     
 */
public interface BaiduVoiceService {

	/**
	 * 获取token
	* @Title: getToken 
	* @return
	 */
	String getToken() throws GlobalException;
	
	/**
	 *文字转语音
	* @Title: text2audio 
	* @param text 需要转换的文本
	* @return
	 */
	String text2audio(String text) throws Exception;
}

/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */

package com.jeecms.wechat.dao.ext;

import java.util.Date;
import java.util.List;

/**
 * WechatSendDaoExt
 * @author: ljw
 * @date: 2018年8月21日 上午11:05:40
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.WechatSend;

public interface WechatSendDaoExt {

	/**
	 * 推送记录分页
	* @Title: getLogPage 
	* @param appids 公众号IDs
	* @param status 发送状态
	* @param startTime 开始时间
	* @param endTime 结束时间
	* @param title 图文标题
	* @param pageable 分页
	* @return Page 分页对象
	* @throws GlobalException 异常
	 */
	Page<WechatSend> getSendPage(List<String> appids, Date startTime, Date endTime,
			Integer status, String title,Pageable pageable) throws GlobalException;
	
}

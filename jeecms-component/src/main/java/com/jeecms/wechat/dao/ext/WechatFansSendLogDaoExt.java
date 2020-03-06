/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */

package com.jeecms.wechat.dao.ext;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.wechat.domain.WechatFansSendLog;

/**
 * WechatFansSendLogDaoExt
 * @author: ljw
 * @date: 2018年8月21日 上午11:05:40
 */
public interface WechatFansSendLogDaoExt {

	/**
	 * 消息记录分页
	* @Title: getLogPage 
	* @param appids 公众号IDs
	* @param type 推送类型
	* @param startTime 开始时间
	* @param endTime 结束时间
	* @param title 图文标题
	* @param black 是否黑名单
	* @param pageable 分页
	* @param openId 粉丝openId
	* @return Page 分页对象
	* @throws GlobalException 异常
	 */
	Page<WechatFansSendLog> getLogPage(List<String> appids, Integer type, Date startTime, Date endTime,
			String title,Boolean black, String openId, Pageable pageable) throws GlobalException;
	
	/**
	 * 得到最新的集合
	* @Title: getList 
	* @param appids 微信公众号IDs
	* @param type 发送类型
	* @param paginable 列表
	* @return List
	* @throws GlobalException 异常
	 */
	List<WechatFansSendLog> getList(List<String> appids, Integer type, Paginable paginable) throws GlobalException;
}

/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.WechatSendDaoExt;
import com.jeecms.wechat.domain.WechatSend;

/**
 * 微信群发Dao
 * @author ljw
 * @version 1.0
 * @date 2018-08-08
 */
public interface WechatSendDao extends IBaseDao<WechatSend, Integer>,WechatSendDaoExt {

	/**
	 * 根据appid以及日期查询群发设置列表
	 * 
	 * @param appid 公众号ID
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param status 发送状态
	 * @return
	 */
	@Query("select bean from WechatSend bean where bean.hasDeleted = 0 and bean.appId in ?1 and bean.sendDate>=?2 "
			+ "and bean.sendDate<=?3 and bean.status =?4")
	Page<WechatSend> selectMassService(List<String> appid, Date startDate, Date endDate, 
			Integer status, Pageable pageable);
	
	/**
	 * 根据appid以及日期查询群发设置列表
	 * 
	 * @param appid 公众号ID
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	@Query("select bean from WechatSend bean where bean.hasDeleted = 0 and bean.appId in ?1 and bean.sendDate>=?2 "
			+ "and bean.sendDate<=?3")
	List<WechatSend> selectMassService(List<String> appid, Date startDate, Date endDate);

}

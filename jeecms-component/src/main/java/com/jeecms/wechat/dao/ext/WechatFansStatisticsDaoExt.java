/**
 * * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */

package com.jeecms.wechat.dao.ext;

import java.util.Date;
import java.util.List;

import com.jeecms.wechat.domain.vo.WechatStatisticsVO;

/** 微信粉丝统计DAO扩展
 * @Description:WechatFansDaoExt
 * @author: ljw
 * @date: 2019年06月03日 
 */
public interface WechatFansStatisticsDaoExt {

	/**
	 * 分组粉丝信息
	 * @param appIds 公众号IDs
	 * @param startTime 开始时间
	 * @param endTime  结束时间
	 * @return
	 */
	List<WechatStatisticsVO> groupFans(List<String> appIds, Date startTime, Date endTime);
}

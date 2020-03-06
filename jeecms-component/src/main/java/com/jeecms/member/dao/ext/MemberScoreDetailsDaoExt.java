/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.member.dao.ext;

import java.util.Date;

/**   
 * 会员积分详情EXT
 * @author: ljw
 * @date:   2019年9月23日 上午11:57:57     
 */
public interface MemberScoreDetailsDaoExt {

	/**
	 * 得到特定时间的积分明细sum
	* @Title: scoreSum 
	* @param userId 用户ID
	* @param startTime 开始时间
	* @param endTime 结束时间
	* @return
	 */
	Integer scoreSum(Integer userId, Date startTime, Date endTime);
}

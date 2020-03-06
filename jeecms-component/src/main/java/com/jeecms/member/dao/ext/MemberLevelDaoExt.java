/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao.ext;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.member.domain.MemberLevel;

/**
 * 等级会员扩展
 * @author: ztx
 * @date:   2018年6月7日 下午5:37:09     
 */
public interface MemberLevelDaoExt {
	
	/**
	 * 根据等级积分获取等级
	 * @Title: findBySecore  
	 * @param secore 积分
	 * @throws GlobalException 全局异常     
	 * @return: MemberLevel 等级对象
	 */
	MemberLevel findBySecore(Integer secore) throws GlobalException;
}

/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.member.dao.ext.MemberScoreDetailsDaoExt;
import com.jeecms.member.domain.MemberScoreDetails;


/**
 * 会员积分详情Dao
* @author ljw
* @version 1.0
* @date 2019-09-23
*/
public interface MemberScoreDetailsDao extends IBaseDao<MemberScoreDetails, Integer>, 
		MemberScoreDetailsDaoExt {

}

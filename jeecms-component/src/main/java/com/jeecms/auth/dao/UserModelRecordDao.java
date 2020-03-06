/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.auth.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.auth.dao.ext.UserModelRecordDaoExt;
import com.jeecms.auth.domain.UserModelRecord;


/**
 * Dao层
* @author ljw
* @version 1.0
* @date 2019-12-13
*/
public interface UserModelRecordDao extends IBaseDao<UserModelRecord, Integer>, UserModelRecordDaoExt {

}

/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.statistics.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.statistics.dao.ext.StaAccessDaoExt;
import com.jeecms.statistics.domain.StatisticsAccess;


/**
 * 忠诚度Dao
* @author ljw
* @version 1.0
* @date 2019-06-25
*/
public interface StaAccessDao extends IBaseDao<StatisticsAccess, Integer>,StaAccessDaoExt {
	
}

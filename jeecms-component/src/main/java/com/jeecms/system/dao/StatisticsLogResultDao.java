/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.StatisticsLogResultDaoExt;
import com.jeecms.system.domain.StatisticsLogResult;


/**
 * 日志请求结果Dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-17
 */
public interface StatisticsLogResultDao extends IBaseDao<StatisticsLogResult, Integer>, StatisticsLogResultDaoExt {

}

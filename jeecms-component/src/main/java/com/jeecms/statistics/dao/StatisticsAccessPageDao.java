/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.statistics.dao.ext.StatisticsAccessPageDaoExt;
import com.jeecms.statistics.domain.StatisticsAccessPage;


/**
 * 受访分析Dao实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-02
 */
public interface StatisticsAccessPageDao extends IBaseDao<StatisticsAccessPage, Integer>, StatisticsAccessPageDaoExt {

}

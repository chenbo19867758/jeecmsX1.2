/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.statistics.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.statistics.dao.ext.StatisticsFlowDaoExt;
import com.jeecms.statistics.domain.StatisticsFlow;

/**
 * 趋势统计dao接口
 * @author: chenming
 * @date:   2019年6月25日 下午2:11:27
 */
public interface StatisticsFlowDao extends IBaseDao<StatisticsFlow, Integer>,StatisticsFlowDaoExt {
	
	List<StatisticsFlow> findBySiteId(Integer siteId);
}	

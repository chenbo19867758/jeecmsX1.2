/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.statistics.dao.ext;

import java.util.Date;
import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.statistics.domain.StatisticsFlow;
import com.jeecms.statistics.domain.dto.StatisticsFlowDto;

public interface StatisticsFlowDaoExt {
	
	List<StatisticsFlow> getFlow(StatisticsFlowDto dto);
	
	/**
	 * 获取趋势列表
	* @Title: flowList 
	* @param start 开始时间
	* @param end 结束时间
	* @param siteId 站点ID
	* @param sourceType 搜索来源
	* @param visit 是否新客户
	* @param province 省份
	* @return List 列表
	* @throws GlobalException 异常
	 */
	List<StatisticsFlow> getFlowList(Date start, Date end, Integer siteId, Integer sourceType, 
			Boolean visit, String province) throws GlobalException;
}

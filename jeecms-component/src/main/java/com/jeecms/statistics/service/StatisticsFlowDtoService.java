package com.jeecms.statistics.service;

import java.util.List;

import com.jeecms.statistics.domain.vo.StatisticsFlowListVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowListVos;

/**
 * 趋势分析service接口
 * @author: chenming
 * @date:   2019年7月4日 下午2:14:24
 */
public interface StatisticsFlowDtoService {
	
	StatisticsFlowListVos initFlowListVos(List<StatisticsFlowListVo> vos);
}

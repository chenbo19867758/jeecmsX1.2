package com.jeecms.statistics.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.util.MathUtil;
import com.jeecms.statistics.domain.vo.StatisitcsOverviewVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowListVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowListVos;
import com.jeecms.statistics.service.StatisticsFlowDtoService;

/**
 * 趋势分析service实现类
 * @author: chenming
 * @date:   2019年7月4日 下午2:15:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsFlowDtoServiceImpl implements StatisticsFlowDtoService{

	@Override
	public StatisticsFlowListVos initFlowListVos(List<StatisticsFlowListVo> vos) {
		StatisticsFlowListVos vo = new StatisticsFlowListVos();
		StatisitcsOverviewVo overviewVo = null;
		if (vos != null && vos.size() > 0) {
			Integer pv = vos.stream().collect(Collectors.summingInt(StatisticsFlowListVo::getPvNum));
			Integer uv = vos.stream().collect(Collectors.summingInt(StatisticsFlowListVo::getUvNum));
			Integer ip = vos.stream().collect(Collectors.summingInt(StatisticsFlowListVo::getIpNum));
			BigDecimal decimal = new BigDecimal(0);
			for (StatisticsFlowListVo flowListVo : vos) {
				if (flowListVo.getDepthNum() != null) {
					decimal = decimal.add(flowListVo.getDepthNum());
				}
			}
			decimal = decimal.divide(new BigDecimal(vos.size()),6);
			Integer time = vos.stream().collect(Collectors.summingInt(StatisticsFlowListVo::getTimeNum));
			overviewVo = new StatisitcsOverviewVo(pv, uv, ip, MathUtil.formatScaleCast(decimal, 4), time/vos.size());
		} else {
			overviewVo = new StatisitcsOverviewVo(0, 0, 0, new BigDecimal(0), 0);
		}
		vo.setTotal(overviewVo);
		return vo;
	}

	
}

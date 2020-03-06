package com.jeecms.statistics.domain.vo;

import org.springframework.data.domain.PageImpl;

/**
 * 分页的StatisticsFlowListVo集合数据+集合数据的汇总
 * 
 * @author: chenming
 * @date: 2019年7月5日 下午1:59:51
 */
public class StatisticsFlowListVos {
	/** 分页的列表数据 */
	private PageImpl<StatisticsFlowListVo> vos;
	/** 汇总数据 */
	private StatisitcsOverviewVo total;

	public PageImpl<StatisticsFlowListVo> getVos() {
		return vos;
	}

	public void setVos(PageImpl<StatisticsFlowListVo> vos) {
		this.vos = vos;
	}

	public StatisitcsOverviewVo getTotal() {
		return total;
	}

	public void setTotal(StatisitcsOverviewVo total) {
		this.total = total;
	}

}

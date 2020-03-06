package com.jeecms.statistics.domain.vo;

import com.alibaba.fastjson.JSONArray;

/**
 * 趋势统计列表数据集
 * 
 * @author: chenming
 * @date: 2019年7月5日 下午2:59:34
 */
public class StatisticsFlowImageVo {
	
	private JSONArray array;
	/** 合集对象 */
	private StatisitcsOverviewVo total;

	public StatisticsFlowImageVo(JSONArray array, StatisitcsOverviewVo total) {
		super();
		this.array = array;
		this.total = total;
	}

	public JSONArray getArray() {
		return array;
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public StatisitcsOverviewVo getTotal() {
		return total;
	}

	public void setTotal(StatisitcsOverviewVo total) {
		this.total = total;
	}

}

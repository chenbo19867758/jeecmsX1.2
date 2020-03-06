package com.jeecms.statistics.domain.dto;

import java.util.List;

/**
 * 组装数据dto
 * @author: chenming
 * @date:   2019年6月26日 上午11:59:39
 */
public class StatisticsFlowSplicDto {
	/** 数据list集合*/
	private List<StatisticsFlowNumDto> numDtos;
	/** 只访问一次页面的访问次数集合*/
	private List<StatisticsFlowNumDto> onlyOnePvs;
	/** 总访问时长，单位：秒*/
	private long time;

	public List<StatisticsFlowNumDto> getNumDtos() {
		return numDtos;
	}

	public void setNumDtos(List<StatisticsFlowNumDto> numDtos) {
		this.numDtos = numDtos;
	}

	public List<StatisticsFlowNumDto> getOnlyOnePvs() {
		return onlyOnePvs;
	}

	public void setOnlyOnePvs(List<StatisticsFlowNumDto> onlyOnePvs) {
		this.onlyOnePvs = onlyOnePvs;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}

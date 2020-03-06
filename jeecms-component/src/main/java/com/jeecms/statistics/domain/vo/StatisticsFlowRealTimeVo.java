package com.jeecms.statistics.domain.vo;

/**
 * 实时访客vo
 * 
 * @author: chenming
 * @date: 2019年7月1日 上午9:24:25
 */
public class StatisticsFlowRealTimeVo {
	/** 时间数组 */
	private String time;
	/** PV数组 */
	private Integer pv;
	/** UV数组 */
	private Integer uv;

	public StatisticsFlowRealTimeVo(String time, Integer pv, Integer uv) {
		super();
		this.time = time;
		this.pv = pv;
		this.uv = uv;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}

}

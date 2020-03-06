package com.jeecms.statistics.domain.vo;

/**
 * 
 * @author: chenming
 * @date:   2019年7月3日 下午5:45:27
 */
public class StatisitcsOverviewVos {
	/** 今天*/
	private StatisitcsOverviewVo  now;
	/** 昨天*/
	private StatisitcsOverviewVo yesterday;
	/** 最高*/
	private StatisitcsOverviewVo highest;

	
	
	public StatisitcsOverviewVos() {
		super();
	}

	public StatisitcsOverviewVos(StatisitcsOverviewVo now, StatisitcsOverviewVo yesterday,
			StatisitcsOverviewVo highest) {
		super();
		this.now = now;
		this.yesterday = yesterday;
		this.highest = highest;
	}

	public StatisitcsOverviewVo getNow() {
		return now;
	}

	public void setNow(StatisitcsOverviewVo now) {
		this.now = now;
	}

	public StatisitcsOverviewVo getYesterday() {
		return yesterday;
	}

	public void setYesterday(StatisitcsOverviewVo yesterday) {
		this.yesterday = yesterday;
	}

	public StatisitcsOverviewVo getHighest() {
		return highest;
	}

	public void setHighest(StatisitcsOverviewVo highest) {
		this.highest = highest;
	}
	
	
}

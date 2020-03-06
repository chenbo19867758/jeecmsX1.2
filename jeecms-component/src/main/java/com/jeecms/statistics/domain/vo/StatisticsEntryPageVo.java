/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * 入口页面Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/3 15:29
 */

public class StatisticsEntryPageVo {
	/**
	 * pv数量
	 */
	private int pvs;
	/**
	 * uv数量
	 */
	private int uvs;
	/**
	 * 贡献下游浏览量
	 */
	private int pageViews;
	/**
	 * 跳出率
	 */
	private BigDecimal bounce;
	/**
	 * 平均访问时长
	 */
	private long average;
	/**
	 * 平均访问时长（HH:mm:ss）
	 */
	private String averageTime;

	private List<StatisticsPageListVo> list;

	private PageImpl<StatisticsPageListVo> page;

	public int getPvs() {
		return pvs;
	}

	public void setPvs(int pvs) {
		this.pvs = pvs;
	}

	public int getUvs() {
		return uvs;
	}

	public void setUvs(int uvs) {
		this.uvs = uvs;
	}

	public int getPageViews() {
		return pageViews;
	}

	public void setPageViews(int pageViews) {
		this.pageViews = pageViews;
	}

	public BigDecimal getBounce() {
		return bounce;
	}

	public void setBounce(BigDecimal bounce) {
		this.bounce = bounce;
	}

	public long getAverage() {
		return average;
	}

	public void setAverage(long average) {
		this.average = average;
	}

	public String getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}

	public List<StatisticsPageListVo> getList() {
		return list;
	}

	public void setList(List<StatisticsPageListVo> list) {
		this.list = list;
	}

	public PageImpl<StatisticsPageListVo> getPage() {
		return page;
	}

	public void setPage(PageImpl<StatisticsPageListVo> page) {
		this.page = page;
	}
}

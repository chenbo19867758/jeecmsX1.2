/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 来源访问列表Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/4 10:04
 */

public class StatisticsSourceListVo {
	/**
	 * 浏览量（PV）
	 */
	private int pvs;
	/**
	 * 访客数（UV）
	 */
	private int uvs;
	/**
	 * ip数
	 */
	private int ips;
	/**
	 * 跳出率
	 */
	private BigDecimal bounce;
	/**
	 * 平均访问时长
	 */
	private long average;
	/**
	 * 搜索引擎
	 */
	private String name;

	/**
	 * 占比
	 */
	private BigDecimal proportion;

	private List<StatisticsSourceListVo> list;

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

	public int getIps() {
		return ips;
	}

	public void setIps(int ips) {
		this.ips = ips;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getProportion() {
		return proportion;
	}

	public void setProportion(BigDecimal proportion) {
		this.proportion = proportion;
	}

	public List<StatisticsSourceListVo> getList() {
		return list;
	}

	public void setList(List<StatisticsSourceListVo> list) {
		this.list = list;
	}
}

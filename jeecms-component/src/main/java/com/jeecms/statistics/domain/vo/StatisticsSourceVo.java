/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * 来源分析Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/4 9:56
 */

public class StatisticsSourceVo {
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
	 * pv总计
	 */
	private int pvTotal;
	/**
	 * uv总计
	 */
	private int uvTotal;
	/**
	 * ip总计
	 */
	private int ipTotal;
	/**
	 * 跳出率总计
	 */
	private BigDecimal bounceTotal;
	/**
	 * 平均访问时长总计
	 */
	private long averageTotal;

	private List<StatisticsSourceListVo> list;

	private Page<StatisticsSourceListVo> page;

	private List<StatisticsSourceTableVo> table;

	private String[] fields;

	private JSONArray objects;

	private List<StatisticsSourcePieVo> pie;

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

	public int getPvTotal() {
		return pvTotal;
	}

	public void setPvTotal(int pvTotal) {
		this.pvTotal = pvTotal;
	}

	public int getUvTotal() {
		return uvTotal;
	}

	public void setUvTotal(int uvTotal) {
		this.uvTotal = uvTotal;
	}

	public int getIpTotal() {
		return ipTotal;
	}

	public void setIpTotal(int ipTotal) {
		this.ipTotal = ipTotal;
	}

	public BigDecimal getBounceTotal() {
		return bounceTotal;
	}

	public void setBounceTotal(BigDecimal bounceTotal) {
		this.bounceTotal = bounceTotal;
	}

	public long getAverageTotal() {
		return averageTotal;
	}

	public void setAverageTotal(long averageTotal) {
		this.averageTotal = averageTotal;
	}

	public List<StatisticsSourceListVo> getList() {
		return list;
	}

	public void setList(List<StatisticsSourceListVo> list) {
		this.list = list;
	}

	public Page<StatisticsSourceListVo> getPage() {
		return page;
	}

	public void setPage(Page<StatisticsSourceListVo> page) {
		this.page = page;
	}

	public List<StatisticsSourceTableVo> getTable() {
		return table;
	}

	public void setTable(List<StatisticsSourceTableVo> table) {
		this.table = table;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public JSONArray getObjects() {
		return objects;
	}

	public void setObjects(JSONArray objects) {
		this.objects = objects;
	}

	public List<StatisticsSourcePieVo> getPie() {
		return pie;
	}

	public void setPie(List<StatisticsSourcePieVo> pie) {
		this.pie = pie;
	}
}

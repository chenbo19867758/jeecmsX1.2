package com.jeecms.statistics.domain.vo;

import java.math.BigDecimal;

/**
 * list分页列表数据
 * 
 * @author: chenming
 * @date: 2019年7月5日 下午1:57:42
 */
public class StatisticsFlowListVo {
	/** 序号 */
	private Integer id;
	/** 日期 */
	private String date;
	/** PV值 */
	private Integer pvNum;
	/** UV值 */
	private Integer uvNum;
	/** IP数量 */
	private Integer ipNum;
	/** 跳出率（精确到小数点后4位） */
	private BigDecimal depthNum;
	/** 平均访问时长(单位/s) */
	private Integer timeNum;

	public StatisticsFlowListVo(Integer id, String date) {
		super();
		this.id = id;
		this.date = date;
	}

	public StatisticsFlowListVo(Integer id, String date, Integer pvNum, Integer uvNum, Integer ipNum,
			BigDecimal depthNum, Integer timeNum) {
		super();
		this.id = id;
		this.date = date;
		this.pvNum = pvNum;
		this.uvNum = uvNum;
		this.ipNum = ipNum;
		this.depthNum = depthNum;
		this.timeNum = timeNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getPvNum() {
		return pvNum;
	}

	public void setPvNum(Integer pvNum) {
		this.pvNum = pvNum;
	}

	public Integer getUvNum() {
		return uvNum;
	}

	public void setUvNum(Integer uvNum) {
		this.uvNum = uvNum;
	}

	public Integer getIpNum() {
		return ipNum;
	}

	public void setIpNum(Integer ipNum) {
		this.ipNum = ipNum;
	}

	public BigDecimal getDepthNum() {
		return depthNum;
	}

	public void setDepthNum(BigDecimal depthNum) {
		this.depthNum = depthNum;
	}

	public Integer getTimeNum() {
		return timeNum;
	}

	public void setTimeNum(Integer timeNum) {
		this.timeNum = timeNum;
	}

}

/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import java.math.BigDecimal;

/**
 * 来源分析饼图
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/2 10:49
 */

public class StatisticsSourcePieVo {

	/**
	 * pv数
	 */
	private int pv;
	/**
	 * pv占比
	 */
	private BigDecimal pvPercentage;
	/**
	 * uv数
	 */
	private int uv;
	/**
	 * uv占比
	 */
	private BigDecimal uvPercentage;
	/**
	 * ip数
	 */
	private int ip;
	/**
	 * ip占比
	 */
	private BigDecimal ipPercentage;
	/**
	 * 分类名
	 */
	private String name;

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public BigDecimal getPvPercentage() {
		return pvPercentage;
	}

	public void setPvPercentage(BigDecimal pvPercentage) {
		this.pvPercentage = pvPercentage;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public BigDecimal getUvPercentage() {
		return uvPercentage;
	}

	public void setUvPercentage(BigDecimal uvPercentage) {
		this.uvPercentage = uvPercentage;
	}

	public int getIp() {
		return ip;
	}

	public void setIp(int ip) {
		this.ip = ip;
	}

	public BigDecimal getIpPercentage() {
		return ipPercentage;
	}

	public void setIpPercentage(BigDecimal ipPercentage) {
		this.ipPercentage = ipPercentage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

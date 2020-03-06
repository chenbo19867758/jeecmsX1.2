/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import java.io.Serializable;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/24 14:31
 */

public class SiteFlow implements Serializable {

	/**
	 * 累计pv
	 */
	private Long pvTotal;
	/**
	 * 累计uv
	 */
	private Long uvTotal;
	/**
	 * 累计ip
	 */
	private Long ipTotal;
	/**
	 * 今日pv
	 */
	private Long todayPv;
	/**
	 * 今日uv
	 */
	private Long todayUv;
	/**
	 * 今日ip
	 */
	private Long todayIp;
	/**
	 * 昨日pv
	 */
	private Long yesterdayPv;
	/**
	 * 昨日uv
	 */
	private Long yesterdayUv;
	/**
	 * 昨日ip
	 */
	private Long yesterdayIp;
	/**
	 * 峰值pv
	 */
	private Long peakPv;
	/**
	 * 峰值uv
	 */
	private Long peakUv;
	/**
	 * 峰值ip
	 */
	private Long peakIp;

	public SiteFlow() {
		super();
	}

	public SiteFlow(Long pvTotal,
					Long uvTotal,
					Long ipTotal,
					Long todayPv,
					Long todayUv,
					Long todayIp,
					Long yesterdayPv,
					Long yesterdayUv,
					Long yesterdayIp,
					Long peakPv,
					Long peakUv,
					Long peakIp) {
		this.pvTotal = pvTotal;
		this.uvTotal = uvTotal;
		this.ipTotal = ipTotal;
		this.todayPv = todayPv;
		this.todayUv = todayUv;
		this.todayIp = todayIp;
		this.yesterdayPv = yesterdayPv;
		this.yesterdayUv = yesterdayUv;
		this.yesterdayIp = yesterdayIp;
		this.peakPv = peakPv;
		this.peakUv = peakUv;
		this.peakIp = peakIp;
	}

	public Long getPvTotal() {
		return pvTotal;
	}

	public void setPvTotal(Long pvTotal) {
		this.pvTotal = pvTotal;
	}

	public Long getUvTotal() {
		return uvTotal;
	}

	public void setUvTotal(Long uvTotal) {
		this.uvTotal = uvTotal;
	}

	public Long getIpTotal() {
		return ipTotal;
	}

	public void setIpTotal(Long ipTotal) {
		this.ipTotal = ipTotal;
	}

	public Long getTodayPv() {
		return todayPv;
	}

	public void setTodayPv(Long todayPv) {
		this.todayPv = todayPv;
	}

	public Long getTodayUv() {
		return todayUv;
	}

	public void setTodayUv(Long todayUv) {
		this.todayUv = todayUv;
	}

	public Long getTodayIp() {
		return todayIp;
	}

	public void setTodayIp(Long todayIp) {
		this.todayIp = todayIp;
	}

	public Long getYesterdayPv() {
		return yesterdayPv;
	}

	public void setYesterdayPv(Long yesterdayPv) {
		this.yesterdayPv = yesterdayPv;
	}

	public Long getYesterdayUv() {
		return yesterdayUv;
	}

	public void setYesterdayUv(Long yesterdayUv) {
		this.yesterdayUv = yesterdayUv;
	}

	public Long getYesterdayIp() {
		return yesterdayIp;
	}

	public void setYesterdayIp(Long yesterdayIp) {
		this.yesterdayIp = yesterdayIp;
	}

	public Long getPeakPv() {
		return peakPv;
	}

	public void setPeakPv(Long peakPv) {
		this.peakPv = peakPv;
	}

	public Long getPeakUv() {
		return peakUv;
	}

	public void setPeakUv(Long peakUv) {
		this.peakUv = peakUv;
	}

	public Long getPeakIp() {
		return peakIp;
	}

	public void setPeakIp(Long peakIp) {
		this.peakIp = peakIp;
	}
}

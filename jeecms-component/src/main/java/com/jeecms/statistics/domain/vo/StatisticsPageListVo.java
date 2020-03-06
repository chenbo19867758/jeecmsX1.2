/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import java.math.BigDecimal;

/**
 * 受访分析列表Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/3 16:58
 */

public class StatisticsPageListVo {
	/**
	 * pv数量
	 */
	private int pvs;
	/**
	 * uv数量
	 */
	private int uvs;
	/**
	 * 跳出率
	 */
	private BigDecimal bounce;
	/**
	 * 贡献下游浏览量
	 */
	private int pageViews;
	/**
	 * 平均停留时长
	 */
	private long average;
	/**
	 * 平均停留时长（HH:mm:ss）
	 */
	private String averageTime;
	/**
	 * 页面链接
	 */
	private String url;

	/**
	 * 占比
	 */
	private BigDecimal proportion;

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

	public BigDecimal getBounce() {
		return bounce;
	}

	public void setBounce(BigDecimal bounce) {
		this.bounce = bounce;
	}

	public int getPageViews() {
		return pageViews;
	}

	public void setPageViews(int pageViews) {
		this.pageViews = pageViews;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BigDecimal getProportion() {
		return proportion;
	}

	public void setProportion(BigDecimal proportion) {
		this.proportion = proportion;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StatisticsPageListVo)) {
			return false;
		}

		StatisticsPageListVo that = (StatisticsPageListVo) o;

		if (getPvs() != that.getPvs()) {
			return false;
		}
		if (getUvs() != that.getUvs()) {
			return false;
		}
		if (getPageViews() != that.getPageViews()) {
			return false;
		}
		if (getAverage() != that.getAverage()) {
			return false;
		}
		if (getBounce() != null ? !getBounce().equals(that.getBounce()) :
				that.getBounce() != null) {
			return false;
		}
		if (getAverageTime() != null ? !getAverageTime().equals(that.getAverageTime()) :
				that.getAverageTime() != null) {
			return false;
		}
		if (getUrl() != null ? !getUrl().equals(that.getUrl()) :
				that.getUrl() != null) {
			return false;
		}
		return getProportion() != null ? getProportion().equals(that.getProportion()) : that.getProportion() == null;
	}

	@Override
	public int hashCode() {
		int result = getPvs();
		result = 31 * result + getUvs();
		result = 31 * result + (getBounce() != null ? getBounce().hashCode() : 0);
		result = 31 * result + getPageViews();
		result = 31 * result + (int) (getAverage() ^ (getAverage() >>> 32));
		result = 31 * result + (getAverageTime() != null ? getAverageTime().hashCode() : 0);
		result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
		result = 31 * result + (getProportion() != null ? getProportion().hashCode() : 0);
		return result;
	}
}

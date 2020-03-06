/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * 受访页面
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/3 9:39
 */

public class StatisticsAccessPageVo {

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
	 * 平均停留时长
	 */
	private long average;
	/**
	 * 平均停留时长（HH:mm:ss）
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StatisticsAccessPageVo)) {
			return false;
		}

		StatisticsAccessPageVo vo = (StatisticsAccessPageVo) o;

		if (getPvs() != vo.getPvs()) {
			return false;
		}
		if (getUvs() != vo.getUvs()) {
			return false;
		}
		if (getPageViews() != vo.getPageViews()) {
			return false;
		}
		if (getAverage() != vo.getAverage()) {
			return false;
		}
		if (getAverageTime() != null ? !getAverageTime().equals(vo.getAverageTime()) :
				vo.getAverageTime() != null) {
			return false;
		}
		if (getList() != null ? !getList().equals(vo.getList()) :
				vo.getList() != null) {
			return false;
		}
		return getPage() != null ? getPage().equals(vo.getPage()) : vo.getPage() == null;
	}

	@Override
	public int hashCode() {
		int result = getPvs();
		result = 31 * result + getUvs();
		result = 31 * result + getPageViews();
		result = 31 * result + (int) (getAverage() ^ (getAverage() >>> 32));
		result = 31 * result + (getAverageTime() != null ? getAverageTime().hashCode() : 0);
		result = 31 * result + (getList() != null ? getList().hashCode() : 0);
		result = 31 * result + (getPage() != null ? getPage().hashCode() : 0);
		return result;
	}
}

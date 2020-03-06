/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

/**
 * 来源访问图表Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/4 10:48
 */

public class StatisticsSourceTableVo {
	/**
	 * 浏览量（PV）
	 */
	private int pvs;
	/**
	 * pv占比
	 */
	private double pvProportion;
	/**
	 * 访客数（UV）
	 */
	private int uvs;
	/**
	 * uv占比
	 */
	private double uvProportion;
	/**
	 * ip数
	 */
	private int ips;
	/**
	 * ip占比
	 */
	private double ipProportion;
	/**
	 * 分类名
	 */
	private String name;
	/**
	 * 搜索引擎总计
	 */
	private int total;
	/**
	 * 时间分组
	 */
	private String time;

	public StatisticsSourceTableVo() {
		super();
	}

	/**
	 * 来源访问图表赋值
	 *
	 * @param pvs          pv数
	 * @param pvProportion pv占比
	 * @param uvs          uv数
	 * @param uvProportion uv占比
	 * @param ips          ip数
	 * @param ipProportion ip占比
	 * @param name         类别名
	 * @param total        总数
	 * @param time         日期
	 */
	public StatisticsSourceTableVo(int pvs, double pvProportion,
								   int uvs, double uvProportion,
								   int ips, double ipProportion,
								   String name, int total,
								   String time) {
		this.pvs = pvs;
		this.pvProportion = pvProportion;
		this.uvs = uvs;
		this.uvProportion = uvProportion;
		this.ips = ips;
		this.ipProportion = ipProportion;
		this.name = name;
		this.total = total;
		this.time = time;
	}

	public int getPvs() {
		return pvs;
	}

	public void setPvs(int pvs) {
		this.pvs = pvs;
	}

	public double getPvProportion() {
		return pvProportion;
	}

	public void setPvProportion(double pvProportion) {
		this.pvProportion = pvProportion;
	}

	public int getUvs() {
		return uvs;
	}

	public void setUvs(int uvs) {
		this.uvs = uvs;
	}

	public double getUvProportion() {
		return uvProportion;
	}

	public void setUvProportion(double uvProportion) {
		this.uvProportion = uvProportion;
	}

	public int getIps() {
		return ips;
	}

	public void setIps(int ips) {
		this.ips = ips;
	}

	public double getIpProportion() {
		return ipProportion;
	}

	public void setIpProportion(double ipProportion) {
		this.ipProportion = ipProportion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StatisticsSourceTableVo)) {
			return false;
		}

		StatisticsSourceTableVo that = (StatisticsSourceTableVo) o;

		if (getPvs() != that.getPvs()) {
			return false;
		}
		if (Double.compare(that.getPvProportion(), getPvProportion()) != 0) {
			return false;
		}
		if (getUvs() != that.getUvs()) {
			return false;
		}
		if (Double.compare(that.getUvProportion(), getUvProportion()) != 0) {
			return false;
		}
		if (getIps() != that.getIps()) {
			return false;
		}
		if (Double.compare(that.getIpProportion(), getIpProportion()) != 0) {
			return false;
		}
		if (getTotal() != that.getTotal()) {
			return false;
		}
		if (getName() != null ? !getName().equals(that.getName()) :
				that.getName() != null) {
			return false;
		}
		return getTime() != null ? getTime().equals(that.getTime()) : that.getTime() == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = getPvs();
		temp = Double.doubleToLongBits(getPvProportion());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + getUvs();
		temp = Double.doubleToLongBits(getUvProportion());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + getIps();
		temp = Double.doubleToLongBits(getIpProportion());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		result = 31 * result + getTotal();
		result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
		return result;
	}
}

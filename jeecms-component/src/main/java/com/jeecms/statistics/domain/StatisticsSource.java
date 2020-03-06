/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain;

import com.jeecms.common.base.domain.AbstractDelFlagDomain;
import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 来源统计实体
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-26
 */
@Entity
@Table(name = "jc_statistics_source")
public class StatisticsSource extends AbstractDelFlagDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**
	 * 站点id
	 */
	private Integer siteId;
	/**
	 * 统计日期（格式:yyyy-MM-dd）
	 */
	private String statisticsDay;
	/**
	 * 来源网站类型 （1-搜索引擎  2-外部链接  3-直接访问）
	 */
	private Integer sorceUrlType;
	/**
	 * 是否新客户 （0-否   1-是）
	 */
	private Boolean isNewVisitor;
	/**
	 * 访客设备类型（1-计算机   2-移动设备）
	 */
	private Integer visitorDeviceType;
	/**
	 * 来源外部链接网站地址或网站名称（如：百度/http://www.jeecms.com）
	 */
	private String sorceUrl;
	/**
	 * 浏览量
	 */
	private Integer pvs;
	/**
	 * 访客数
	 */
	private Integer uvs;
	/**
	 * ip数
	 */
	private Integer ips;
	/**
	 * 总访问时长(单位：秒)
	 */
	private Integer accessHoureLong;
	/**
	 * 只访问一次页面的访问次数
	 */
	private Integer onlyOnePv;
	/**
	 * 时间段
	 */
	private Integer statisticsHour;
	/**
	 * 搜索引擎
	 */
	private String engineName;
	/**
	 * 来源域名
	 */
	private String sourceDomain;

	public StatisticsSource() {
	}

	public StatisticsSource(Integer siteId, String statisticsDay,
							Integer sorceUrlType, Boolean isNewVisitor,
							Integer visitorDeviceType, String sorceUrl,
							Integer pvs, Integer uvs, Integer ips,
							Integer accessHoureLong, Integer onlyOnePv,
							Integer statisticsHour, String engineName,
							String sourceDomain) {
		this.siteId = siteId;
		this.statisticsDay = statisticsDay;
		this.sorceUrlType = sorceUrlType;
		this.isNewVisitor = isNewVisitor;
		this.visitorDeviceType = visitorDeviceType;
		this.sorceUrl = sorceUrl;
		this.pvs = pvs;
		this.uvs = uvs;
		this.ips = ips;
		this.accessHoureLong = accessHoureLong;
		this.onlyOnePv = onlyOnePv;
		this.statisticsHour = statisticsHour;
		this.engineName = engineName;
		this.sourceDomain = sourceDomain;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_statistics_source", pkColumnValue = "jc_statistics_source", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_statistics_source")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Column(name = "statistics_day", nullable = false, length = 13)
	public String getStatisticsDay() {
		return statisticsDay;
	}

	public void setStatisticsDay(String statisticsDay) {
		this.statisticsDay = statisticsDay;
	}

	@Column(name = "sorce_url_type", nullable = false, length = 6)
	public Integer getSorceUrlType() {
		return sorceUrlType;
	}

	public void setSorceUrlType(Integer sorceUrlType) {
		this.sorceUrlType = sorceUrlType;
	}

	@Column(name = "is_new_visitor", nullable = false, length = 1)
	public Boolean getIsNewVisitor() {
		return isNewVisitor;
	}

	public void setIsNewVisitor(Boolean isNewVisitor) {
		this.isNewVisitor = isNewVisitor;
	}

	@Column(name = "visitor_device_type", nullable = false, length = 6)
	public Integer getVisitorDeviceType() {
		return visitorDeviceType;
	}

	public void setVisitorDeviceType(Integer visitorDeviceType) {
		this.visitorDeviceType = visitorDeviceType;
	}

	@Column(name = "sorce_url", nullable = true, length = 500)
	public String getSorceUrl() {
		return sorceUrl;
	}

	public void setSorceUrl(String sorceUrl) {
		this.sorceUrl = sorceUrl;
	}

	@Column(name = "pvs", nullable = false, length = 11)
	public Integer getPvs() {
		return pvs;
	}

	public void setPvs(Integer pvs) {
		this.pvs = pvs;
	}

	@Column(name = "uvs", nullable = false, length = 11)
	public Integer getUvs() {
		return uvs;
	}

	public void setUvs(Integer uvs) {
		this.uvs = uvs;
	}

	@Column(name = "ips", nullable = false, length = 11)
	public Integer getIps() {
		return ips;
	}

	public void setIps(Integer ips) {
		this.ips = ips;
	}

	@Column(name = "access_houre_long", nullable = false, length = 11)
	public Integer getAccessHoureLong() {
		return accessHoureLong;
	}

	public void setAccessHoureLong(Integer accessHoureLong) {
		this.accessHoureLong = accessHoureLong;
	}

	@Column(name = "only_one_pv", nullable = false, length = 11)
	public Integer getOnlyOnePv() {
		return onlyOnePv;
	}

	public void setOnlyOnePv(Integer onlyOnePv) {
		this.onlyOnePv = onlyOnePv;
	}

	@Column(name = "statistics_hour", nullable = false, length = 6)
	public Integer getStatisticsHour() {
		return statisticsHour;
	}

	public void setStatisticsHour(Integer statisticsHour) {
		this.statisticsHour = statisticsHour;
	}

	@Column(name = "engine_name", nullable = true, length = 15)
	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	@Column(name = "source_domain", nullable = true, length = 255)
	public String getSourceDomain() {
		return sourceDomain;
	}

	public void setSourceDomain(String sourceDomain) {
		this.sourceDomain = sourceDomain;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StatisticsSource)) {
			return false;
		}

		StatisticsSource that = (StatisticsSource) o;

		if (getId() != null ? !getId().equals(that.getId()) :
				that.getId() != null) {
			return false;
		}
		if (getSiteId() != null ? !getSiteId().equals(that.getSiteId()) :
				that.getSiteId() != null) {
			return false;
		}
		if (getStatisticsDay() != null ? !getStatisticsDay().equals(that.getStatisticsDay()) :
				that.getStatisticsDay() != null) {
			return false;
		}
		if (getSorceUrlType() != null ? !getSorceUrlType().equals(that.getSorceUrlType()) :
				that.getSorceUrlType() != null) {
			return false;
		}
		if (getIsNewVisitor() != null ? !getIsNewVisitor().equals(that.getIsNewVisitor()) :
				that.getIsNewVisitor() != null) {
			return false;
		}
		if (getVisitorDeviceType() != null ? !getVisitorDeviceType().equals(that.getVisitorDeviceType()) :
				that.getVisitorDeviceType() != null) {
			return false;
		}
		if (getSorceUrl() != null ? !getSorceUrl().equals(that.getSorceUrl()) :
				that.getSorceUrl() != null) {
			return false;
		}
		if (getPvs() != null ? !getPvs().equals(that.getPvs()) :
				that.getPvs() != null) {
			return false;
		}
		if (getUvs() != null ? !getUvs().equals(that.getUvs()) :
				that.getUvs() != null) {
			return false;
		}
		if (getIps() != null ? !getIps().equals(that.getIps()) :
				that.getIps() != null) {
			return false;
		}
		if (getAccessHoureLong() != null ? !getAccessHoureLong().equals(that.getAccessHoureLong()) :
				that.getAccessHoureLong() != null) {
			return false;
		}
		if (getOnlyOnePv() != null ? !getOnlyOnePv().equals(that.getOnlyOnePv()) :
				that.getOnlyOnePv() != null) {
			return false;
		}
		if (getStatisticsHour() != null ? !getStatisticsHour().equals(that.getStatisticsHour()) :
				that.getStatisticsHour() != null) {
			return false;
		}
		if (getEngineName() != null ? !getEngineName().equals(that.getEngineName()) :
				that.getEngineName() != null) {
			return false;
		}
		return getSourceDomain() != null ? getSourceDomain().equals(that.getSourceDomain()) : that.getSourceDomain() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getSiteId() != null ? getSiteId().hashCode() : 0);
		result = 31 * result + (getStatisticsDay() != null ? getStatisticsDay().hashCode() : 0);
		result = 31 * result + (getSorceUrlType() != null ? getSorceUrlType().hashCode() : 0);
		result = 31 * result + (getIsNewVisitor() != null ? getIsNewVisitor().hashCode() : 0);
		result = 31 * result + (getVisitorDeviceType() != null ? getVisitorDeviceType().hashCode() : 0);
		result = 31 * result + (getSorceUrl() != null ? getSorceUrl().hashCode() : 0);
		result = 31 * result + (getPvs() != null ? getPvs().hashCode() : 0);
		result = 31 * result + (getUvs() != null ? getUvs().hashCode() : 0);
		result = 31 * result + (getIps() != null ? getIps().hashCode() : 0);
		result = 31 * result + (getAccessHoureLong() != null ? getAccessHoureLong().hashCode() : 0);
		result = 31 * result + (getOnlyOnePv() != null ? getOnlyOnePv().hashCode() : 0);
		result = 31 * result + (getStatisticsHour() != null ? getStatisticsHour().hashCode() : 0);
		result = 31 * result + (getEngineName() != null ? getEngineName().hashCode() : 0);
		result = 31 * result + (getSourceDomain() != null ? getSourceDomain().hashCode() : 0);
		return result;
	}
}
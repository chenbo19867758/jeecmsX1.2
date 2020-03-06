/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain;

import com.jeecms.common.base.domain.AbstractDelFlagDomain;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.IBaseSite;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-02
 */
@Entity
@Table(name = "jc_statistics_access_page")
public class StatisticsAccessPage extends AbstractDelFlagDomain<Integer> implements Serializable, IBaseSite {
	private static final long serialVersionUID = 1L;

	/**
	 * 受访页面
	 */
	public static int URL_TYPE_ACCESS = 1;
	/**
	 * 入口页面
	 */
	public static int URL_TYPE_ENTRY = 2;

	private Integer id;
	/**
	 * 站点id
	 */
	private Integer siteId;
	/**
	 * 来源网站类型 （1-搜索引擎  2-外部链接  3-直接访问）
	 */
	private Integer sourceType;
	/**
	 * 统计日期（YYYY-MM-dd）
	 */
	private String statisticsDay;
	/**
	 * 是否新客户 （0-否   1-是）
	 */
	private Boolean newVisitor;
	/**
	 * 页面类型（1-受访页面  2-入口页面）
	 */
	private Integer urlType;
	/**
	 * 页面地址
	 */
	private String url;
	/**
	 * 浏览量
	 */
	private Integer pvs;
	/**
	 * 访客数
	 */
	private Integer uvs;
	/**
	 * 贡献下游流量
	 */
	private Integer flows;
	/**
	 * 总访问时长(单位：秒)
	 */
	private Integer accessHoureLong;
	/**
	 * 只访问一次页面的访问次数
	 */
	private Integer onlyOnePv;

	public StatisticsAccessPage() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_statistics_access_page", pkColumnValue = "jc_statistics_access_page", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_statistics_access_page")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Column(name = "source_type", nullable = false, length = 6)
	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "statistics_day", nullable = false, length = 13)
	public String getStatisticsDay() {
		return statisticsDay;
	}

	public void setStatisticsDay(String statisticsDay) {
		this.statisticsDay = statisticsDay;
	}

	@Column(name = "is_new_visitor", nullable = false, length = 6)
	public Boolean getNewVisitor() {
		return newVisitor;
	}

	public void setNewVisitor(Boolean newVisitor) {
		this.newVisitor = newVisitor;
	}

	@Column(name = "url_type", nullable = false, length = 6)
	public Integer getUrlType() {
		return urlType;
	}

	public void setUrlType(Integer urlType) {
		this.urlType = urlType;
	}

	@Column(name = "url", nullable = true, length = 500)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	@Column(name = "flows", nullable = false, length = 11)
	public Integer getFlows() {
		return flows;
	}

	public void setFlows(Integer flows) {
		this.flows = flows;
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


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StatisticsAccessPage)) {
			return false;
		}

		StatisticsAccessPage that = (StatisticsAccessPage) o;

		if (getId() != null ? !getId().equals(that.getId()) :
				that.getId() != null) {
			return false;
		}
		if (getSiteId() != null ? !getSiteId().equals(that.getSiteId()) :
				that.getSiteId() != null) {
			return false;
		}
		if (getSourceType() != null ? !getSourceType().equals(that.getSourceType()) :
				that.getSourceType() != null) {
			return false;
		}
		if (getNewVisitor() != null ? !getNewVisitor().equals(that.getNewVisitor()) :
				that.getNewVisitor() != null) {
			return false;
		}
		if (getUrlType() != null ? !getUrlType().equals(that.getUrlType()) :
				that.getUrlType() != null) {
			return false;
		}
		if (getUrl() != null ? !getUrl().equals(that.getUrl()) :
				that.getUrl() != null) {
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
		if (getFlows() != null ? !getFlows().equals(that.getFlows()) :
				that.getFlows() != null) {
			return false;
		}
		if (getAccessHoureLong() != null ? !getAccessHoureLong().equals(that.getAccessHoureLong()) :
				that.getAccessHoureLong() != null) {
			return false;
		}
		return getOnlyOnePv() != null ? getOnlyOnePv().equals(that.getOnlyOnePv()) : that.getOnlyOnePv() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getSiteId() != null ? getSiteId().hashCode() : 0);
		result = 31 * result + (getSourceType() != null ? getSourceType().hashCode() : 0);
		result = 31 * result + (getNewVisitor() != null ? getNewVisitor().hashCode() : 0);
		result = 31 * result + (getUrlType() != null ? getUrlType().hashCode() : 0);
		result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
		result = 31 * result + (getPvs() != null ? getPvs().hashCode() : 0);
		result = 31 * result + (getUvs() != null ? getUvs().hashCode() : 0);
		result = 31 * result + (getFlows() != null ? getFlows().hashCode() : 0);
		result = 31 * result + (getAccessHoureLong() != null ? getAccessHoureLong().hashCode() : 0);
		result = 31 * result + (getOnlyOnePv() != null ? getOnlyOnePv().hashCode() : 0);
		return result;
	}
}
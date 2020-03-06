package com.jeecms.statistics.domain.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.jeecms.statistics.constants.StatisticsConstant;
import com.jeecms.statistics.domain.StatisticsFlow;

/**
 * 流量趋势分析-接收dto
 * 
 * @author: chenming
 * @date: 2019年7月4日 下午9:47:29
 */
public class StatisticsFlowDto {

	/** 站点id */
	private Integer siteId;
	/** 最小时间，如果最小时间不存在则为查询今天 */
	private Date minTime;
	/** 最大时间 */
	private Date maxTime;
	/** 显示方式：1.小时、2.日、3.周、4.月 */
	private Short showStyle;
	/** 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问） */
	private Integer sorceUrlType;
	/** 地域：全国、具体的省市区、其它 */
	private String city;
	/** 省 */
	private String province;
	/** 区域code值 */
	private String area;
	/** 访问来源(1:PC 2:移动端) */
	private Short accessSourceClient;
	/** 是否新客户 **/
	private Boolean newVisitor;
	/** 排序条件：1.日期，2.浏览量，3.访客数，4.ip，5.跳出率，6.平均访问时长 */
	private Short sortTerm;
	/** true.正序，false.倒序 */
	private Boolean order;
	
	private Integer page;
	
	private Integer size;
	
	
	private Integer showType;

	public Date getMinTime() {
		return minTime;
	}

	public void setMinTime(Date minTime) {
		this.minTime = minTime;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	@Min(1)
	@Max(4)
	@NotNull
	public Short getShowStyle() {
		return showStyle;
	}

	public void setShowStyle(Short showStyle) {
		this.showStyle = showStyle;
	}

	public Integer getSorceUrlType() {
		return sorceUrlType;
	}

	public void setSorceUrlType(Integer sorceUrlType) {
		this.sorceUrlType = sorceUrlType;
	}

	@Null
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Null
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}


	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public Short getAccessSourceClient() {
		return accessSourceClient;
	}

	public void setAccessSourceClient(Short accessSourceClient) {
		this.accessSourceClient = accessSourceClient;
	}

	public Boolean getNewVisitor() {
		return newVisitor;
	}

	public void setNewVisitor(Boolean newVisitor) {
		this.newVisitor = newVisitor;
	}

	public Short getSortTerm() {
		return sortTerm;
	}

	public void setSortTerm(Short sortTerm) {
		this.sortTerm = sortTerm;
	}

	@NotNull
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
	}

	@NotNull
	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public String getEngineName() {
		if (sorceUrlType != null) {
			switch (sorceUrlType) {
			case StatisticsConstant.SORCE_TYPE_ENGINE_BAIDU:
				return "百度搜索";
			case StatisticsConstant.SORCE_TYPE_ENGINE_SO:
				return "360搜索";
			case StatisticsConstant.SORCE_TYPE_ENGINE_SOGOU:
				return "搜狗搜索";
			case StatisticsConstant.SORCE_TYPE_ENGINE_CHINASO:
				return "中国搜索";
			case StatisticsConstant.SORCE_TYPE_ENGINE_BING:
				return "微软搜索";
			case StatisticsConstant.SORCE_TYPE_ENGINE_YAHOO:
				return "雅虎搜索";
			case StatisticsConstant.SORCE_TYPE_ENGINE_GOOGLE:
				return "谷歌搜索";
			case StatisticsConstant.SORCE_TYPE_ENGINE_OTHER:
				return "其它";
			default:
				break;
			}
		}
		return null;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	public boolean getIsCurrent() {
		if (System.currentTimeMillis() >= getMaxTime().getTime()) {
			return true;
		}
		if (getShowStyle() == StatisticsFlow.SHOW_HOUR) {
			return true;
		}
		return false;
	}
}

package com.jeecms.statistics.domain.dto;

/**
 * 趋势分析数据
 * 
 * @author: chenming
 * @date: 2019年6月26日 上午9:16:40
 */
public class StatisticsFlowNumDto {

	/** 站点id */
	private Integer siteId;
	/** 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问） */
	private Integer sorceUrlType;
	/** 访客设备类型（1-计算机 2-移动设备） */
	private Integer visitorDeviceType;
	/** 访客设备操作系统（具体到大版本号即可，如win10 ,iso11） */
	private String visitorDeviceOs;
	/** 访客区域（省份） */
	private String visitorProvince;
	/** 访客区域（城市） */
	private String visitotrCity;
	/** 时间点，0 ，1，2....23共24个小时段 */
	private Integer statisticsHour;
	/** 是否新客户 （0-否 1-是） */
	private Boolean isNewVisitor;
	/** 浏览量 */
	private Long pvs;
	/** 访客数 */
	private Long uvs;
	/** ip数 */
	private Long ips;
	/** 只访问一次页面的访问次数 */
	private Integer accessNum;
	/** 访问时间*/
	private Long time;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getSorceUrlType() {
		return sorceUrlType;
	}

	public void setSorceUrlType(Integer sorceUrlType) {
		this.sorceUrlType = sorceUrlType;
	}

	public Integer getVisitorDeviceType() {
		return visitorDeviceType;
	}

	public void setVisitorDeviceType(Integer visitorDeviceType) {
		this.visitorDeviceType = visitorDeviceType;
	}

	public String getVisitorProvince() {
		return visitorProvince;
	}

	public void setVisitorProvince(String visitorProvince) {
		this.visitorProvince = visitorProvince;
	}

	public String getVisitotrCity() {
		return visitotrCity;
	}

	public void setVisitotrCity(String visitotrCity) {
		this.visitotrCity = visitotrCity;
	}

	public Integer getStatisticsHour() {
		return statisticsHour;
	}

	public void setStatisticsHour(Integer statisticsHour) {
		this.statisticsHour = statisticsHour;
	}

	public Long getPvs() {
		return pvs;
	}

	public void setPvs(Long pvs) {
		this.pvs = pvs;
	}

	public Long getUvs() {
		return uvs;
	}

	public void setUvs(Long uvs) {
		this.uvs = uvs;
	}

	public Long getIps() {
		return ips;
	}

	public void setIps(Long ips) {
		this.ips = ips;
	}

	public Boolean getIsNewVisitor() {
		return isNewVisitor;
	}

	public void setIsNewVisitor(Boolean isNewVisitor) {
		this.isNewVisitor = isNewVisitor;
	}

	public String getVisitorDeviceOs() {
		return visitorDeviceOs;
	}

	public void setVisitorDeviceOs(String visitorDeviceOs) {
		this.visitorDeviceOs = visitorDeviceOs;
	}

	public Integer getAccessNum() {
		return accessNum;
	}

	public void setAccessNum(Integer accessNum) {
		this.accessNum = accessNum;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}

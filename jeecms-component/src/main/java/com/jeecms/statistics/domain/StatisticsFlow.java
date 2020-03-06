/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDelFlagDomain;

/**
 * @author chenming
 * @version 1.0
 * @date 2019-06-26
 */
@Entity
@Table(name = "jc_statistics_flow")
public class StatisticsFlow extends AbstractDelFlagDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Short SOURCE_PC = 1;
	public static final Short SOURCE_MOBILE = 2;

	public static final short SHOW_HOUR = 1;

	public static final short SHOW_DAY = 2;

	public static final short SHOW_WEEK = 3;

	public static final short SHOW_MONTH = 4;

	public static final short SORT_DATE = 1;
	public static final short SORT_PV = 2;
	public static final short SORT_UV = 3;
	public static final short SORT_IP = 4;
	public static final short SORT_DEPTH = 5;
	public static final short SORT_TIME_NUM = 6;

	public static final short SORT_TIME = 1;
	public static final short SORT_DURATION = 2;
	public static final short SORT_PAGE_NUM = 3;

	private Integer id;
	/** 站点id */
	private Integer siteId;
	/** 统计日期（格式:yyyy-MM-dd） */
	private String statisticsDay;
	/** 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问） */
	private Integer sorceUrlType;
	
	/**搜索引擎名称**/
	private String engineName;
	
	/** 访客设备操作系统 **/
	private String visitorDeviceOs;
	/** 是否新客户 （0-否 1-是） */
	private Boolean isNewVisitor;
	/** 访客设备类型（1-计算机 2-移动设备） */
	private Integer visitorDeviceType;
	/** 访客区域（省份） */
	private String visitorProvince;
	/** 访客区域（城市） */
	private String visitorCity;
	/** 时间点，0 ，1，2....23共24个小时段 */
	private Integer statisticsHour;
	/** 浏览量 */
	private Integer pvs;
	/** 访客数 */
	private Integer uvs;
	/** ip数 */
	private Integer ips;
	/** 总访问时长(单位：秒) */
	private Integer accessHoureLong;
	/** 只访问一次页面的访问次数 */
	private Integer onlyOnePv;
	/** session 个数 **/
	private Integer session;
	/** 创建时间 */
	private Date createTime;

	/**
	 * 全参数构建方法
	 */
	public StatisticsFlow(Integer siteId, String statisticsDay, Integer sorceUrlType, 
			Boolean isNewVisitor, Integer visitorDeviceType, String visitorDeviceOs, 
			String visitorProvince, String visitorCity, Integer statisticsHour, 
			Integer pvs, Integer uvs, Integer ips, Integer accessHoureLong, Integer onlyOnePv,
			Integer session,String engineName) {
		super();
		this.siteId = siteId;
		this.statisticsDay = statisticsDay;
		this.sorceUrlType = sorceUrlType;
		this.isNewVisitor = isNewVisitor;
		this.visitorDeviceType = visitorDeviceType;
		this.visitorDeviceOs = visitorDeviceOs;
		this.visitorProvince = visitorProvince;
		this.visitorCity = visitorCity;
		this.statisticsHour = statisticsHour;
		this.pvs = pvs;
		this.uvs = uvs;
		this.ips = ips;
		this.accessHoureLong = accessHoureLong;
		this.onlyOnePv = onlyOnePv;
		this.session = session;
		this.createTime = Calendar.getInstance().getTime();
		this.engineName = engineName;
	}

	public StatisticsFlow() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_statistics_flow", pkColumnValue = "jc_statistics_flow", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_statistics_flow")
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

	@Column(name = "visitor_device_os", nullable = true, length = 50)
	public String getVisitorDeviceOs() {
		return visitorDeviceOs;
	}

	public void setVisitorDeviceOs(String visitorDeviceOs) {
		this.visitorDeviceOs = visitorDeviceOs;
	}

	@Column(name = "visitor_province", nullable = true, length = 50)
	public String getVisitorProvince() {
		return visitorProvince;
	}

	public void setVisitorProvince(String visitorProvince) {
		this.visitorProvince = visitorProvince;
	}

	@Column(name = "visitor_city", nullable = true, length = 50)
	public String getVisitorCity() {
		return visitorCity;
	}

	public void setVisitorCity(String visitorCity) {
		this.visitorCity = visitorCity;
	}

	@Column(name = "statistics_hour", nullable = false, length = 11)
	public Integer getStatisticsHour() {
		return statisticsHour;
	}

	public void setStatisticsHour(Integer statisticsHour) {
		this.statisticsHour = statisticsHour;
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

	@Column(name = "session_count", nullable = false, length = 11)
	public Integer getSession() {
		return session;
	}

	public void setSession(Integer session) {
		this.session = session;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")  
	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "engine_name", nullable = false, length = 50)
	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	public static final Integer COMPUTER = 1;
	public static final Integer MOBIE = 2;
}
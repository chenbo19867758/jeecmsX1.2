/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.statistics.domain;

import java.io.Serializable;

import com.jeecms.common.base.domain.AbstractDelFlagDomain;
import com.jeecms.common.base.domain.AbstractDomain;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 忠诚度统计
 * @author ljw
 * @version 1.0
 * @date 2019-06-25
 * 
 */
@Entity
@Table(name = "jc_statistics_access")
public class StatisticsAccess extends AbstractDelFlagDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 站点id */
	private Integer siteId;
	/** 统计日期（格式:yyyy-MM-dd） */
	private String statisticsDay;
	/** 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问） */
	private Integer sorceUrlType;
	/** 是否新客户 （0-否 1-是） */
	private Boolean isNewVisitor;
	/** 访客区域（精确到省份即可） */
	private String visitorArea;
	/** 访问深度页数，单位：页（1，2，3，4，5，6，7，8 ，9，10，11，16，21） */
	private Integer depthPage;
	/** 访问页数，单位：页（1，2，3，4，5，11，21，50） */
	private Integer accessPage;
	/** 访问时长，单位秒（1，10，30，60，180，600，1800，3600，未知） */
	private Integer accessTime;
	/** 访问次数 */
	private Integer accessCount;

	public StatisticsAccess() {}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_statistics_access", pkColumnValue = "jc_statistics_access", 
				initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_statistics_access")
	public Integer getId() {
		return this.id;
	}

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

	@Column(name = "visitor_area", nullable = true, length = 50)
	public String getVisitorArea() {
		return visitorArea;
	}

	public void setVisitorArea(String visitorArea) {
		this.visitorArea = visitorArea;
	}

	@Column(name = "depth_page", nullable = false, length = 11)
	public Integer getDepthPage() {
		return depthPage;
	}

	public void setDepthPage(Integer depthPage) {
		this.depthPage = depthPage;
	}

	@Column(name = "access_page", nullable = true, length = 10)
	public Integer getAccessPage() {
		return accessPage;
	}

	public void setAccessPage(Integer accessPage) {
		this.accessPage = accessPage;
	}

	@Column(name = "access_time", nullable = true, length = 10)
	public Integer getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Integer accessTime) {
		this.accessTime = accessTime;
	}

	@Column(name = "access_count", nullable = false, length = 11)
	public Integer getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(Integer accessCount) {
		this.accessCount = accessCount;
	}

	/**访问页数为1**/
	public static final Integer PAGE_1 = 1;
	/**访问页数为2**/
	public static final Integer PAGE_2 = 2;
	/**访问页数为3**/
	public static final Integer PAGE_3 = 3;
	/**访问页数为4**/
	public static final Integer PAGE_4 = 4;
	/**访问页数为5**/
	public static final Integer PAGE_5 = 5;
	/**访问页数为6**/
	public static final Integer PAGE_6 = 6;
	/**访问页数为7**/
	public static final Integer PAGE_7 = 7;
	/**访问页数为8**/
	public static final Integer PAGE_8 = 8;
	/**访问页数为9**/
	public static final Integer PAGE_9 = 9;
	/**访问页数为10**/
	public static final Integer PAGE_10 = 10;
	/**访问页数为11**/
	public static final Integer PAGE_11 = 11;
	/**访问页数为15**/
	public static final Integer PAGE_15 = 15;
	/**访问页数为16**/
	public static final Integer PAGE_16 = 16;
	/**访问页数为20**/
	public static final Integer PAGE_20 = 20;
	/**访问页数为21**/
	public static final Integer PAGE_21 = 21;
	/**访问页数为21**/
	public static final Integer PAGE_49 = 49;
	/**访问页数为50以上**/
	public static final Integer PAGE_50 = 50;
	
	/**访问时长0-9秒**/
	public static final int TIME_1 = 1;
	/**访问时长10-29秒**/
	public static final int TIME_2 = 2;
	/**访问时长30-59秒**/
	public static final int TIME_3 = 3;
	/**访问时长1-3分钟**/
	public static final int TIME_4 = 4;
	/**访问时长3-10分钟**/
	public static final int TIME_5 = 5;
	/**访问时长10-30分钟**/
	public static final int TIME_6 = 6;
	/**访问时长30-60分钟**/
	public static final int TIME_7 = 7;
	/**访问时长一小时以上**/
	public static final int TIME_8 = 8;
	/**访问时长未知**/
	public static final int TIME_9 = 9;
	
}
/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.dto;

import java.util.Date;

import com.sun.istack.NotNull;

/**
 * 统计搜索Dto
 * 
 * @author: ljw
 * @date: 2019年6月27日 上午9:37:29
 */
public class StatisticsDto {

	/** 站点ID **/
	private Integer siteId;
	/** 开始时间 **/
	private Long startTime;
	/** 结束时间 **/
	private Long endTime;
	/** 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问） **/
	private Integer sourceType;
	/** 省级 **/
	private String province;
	/** 是否新访客 **/
	private Boolean visitor;
	/**查询类型1.访问页数2.访问深度3.访问时长**/
	private Integer type;
	/**城市名称**/
	private String city;
	/**排序类型1.浏览量2.访客数3.IP数4.跳出率5.平均访问时长**/
	/**忠诚度排序类型：1.访问次数 2.所占比例**/
	private Integer orderType;
	/**排序方式true 正序 flase 倒序**/
	private Boolean order;
	/** 页码 **/
	private Integer page;
	/** 数量 **/
	private Integer size;
	
	public StatisticsDto() {
	}

	@NotNull
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotNull
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@NotNull
	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Boolean getVisitor() {
		return visitor;
	}

	public void setVisitor(Boolean visitor) {
		this.visitor = visitor;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
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

	public static final Integer PAGE_TYPE = 1;
	public static final Integer HIGH_TYPE = 2;
	public static final Integer TIME_TYPE = 3;
	
	public static final Integer ORDER_TYPE_PV = 1;
	public static final Integer ORDER_TYPE_UV = 2;
	public static final Integer ORDER_TYPE_IP = 3;
	public static final Integer ORDER_TYPE_JUMP = 4;
	public static final Integer ORDER_TYPE_TIME = 5;
	public static final Integer ORDER_TYPE_PAGE = 6;
	
	public static final Integer ACCESS_TYPE_NUMBER = 1;
	public static final Integer ACCESS_TYPE_DECIMAL = 2;
}

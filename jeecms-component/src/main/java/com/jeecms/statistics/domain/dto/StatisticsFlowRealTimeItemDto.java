package com.jeecms.statistics.domain.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.jeecms.statistics.constants.StatisticsConstant;

/**
 * 实时访客详情接收dto
 * 
 * @author: chenming
 * @date: 2019年7月1日 上午11:48:57
 */
public class StatisticsFlowRealTimeItemDto {

	/** 站点id */
	private Integer siteId;
	/** 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问） */
	private Integer sorceUrlType;
	/** 地域：全国、具体的省市区、其它 */
	private String city;
	/** 省 */
	private String province;
	/** 区域ID值 */
	private String area;
	/** 访问来源(1:PC 2:移动端) */
	private Short accessSourceClient;
	/** 访问时间 */
	private Date time;
	/** 是否新客户 **/
	private Boolean newVisitor;
	/** 是否登录访问（0-否 1-是） */
	private Boolean isLogin;
	/** 最小的访问频次 */
	private Integer minFrequency;
	/** 最大的访问频次 */
	private Integer maxFrequency;
	/** 最小访问深度 */
	private Integer minDepth;
	/** 最大访问深度 */
	private Integer maxDepth;
	/** 访问时长 */
	private Integer duration;
	/** 入口页面 */
	private String entranceUrl;
	/** 用户名 */
	private String userName;
	/** 访问ip */
	private String accessIp;

	/** 排序条件：1.访问时间、2.访问时长、3.访问页数 */
	private Short sortTerm;
	/** true.正序，false.倒序 */
	private Boolean order;

	private Integer page;
	
	private Integer size;
	
	@NotNull
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Min(1)
	public Integer getSorceUrlType() {
		return sorceUrlType;
	}

	public void setSorceUrlType(Integer sorceUrlType) {
		this.sorceUrlType = sorceUrlType;
	}

	@Min(1)
	@Max(2)
	public Short getAccessSourceClient() {
		return accessSourceClient;
	}

	public void setAccessSourceClient(Short accessSourceClient) {
		this.accessSourceClient = accessSourceClient;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Boolean getNewVisitor() {
		return newVisitor;
	}

	public void setNewVisitor(Boolean newVisitor) {
		this.newVisitor = newVisitor;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

	public Integer getMinFrequency() {
		return minFrequency;
	}

	public void setMinFrequency(Integer minFrequency) {
		this.minFrequency = minFrequency;
	}

	public Integer getMaxFrequency() {
		return maxFrequency;
	}

	public void setMaxFrequency(Integer maxFrequency) {
		this.maxFrequency = maxFrequency;
	}

	public Integer getMinDepth() {
		return minDepth;
	}

	public void setMinDepth(Integer minDepth) {
		this.minDepth = minDepth;
	}

	public Integer getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(Integer maxDepth) {
		this.maxDepth = maxDepth;
	}

	public String getEntranceUrl() {
		return entranceUrl;
	}

	public void setEntranceUrl(String entranceUrl) {
		this.entranceUrl = entranceUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@NotNull
	@Min(1)
	@Max(3)
	public Short getSortTerm() {
		return sortTerm;
	}

	public void setSortTerm(Short sortTerm) {
		this.sortTerm = sortTerm;
	}

	@NotNull
	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
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
		if (getTime() != null) {
			return true;
		}
		return false;
	}
	
	public boolean getIsFrequency() {
		if (getMinFrequency() != null) {
			return true;
		}
		return false;
	}
}

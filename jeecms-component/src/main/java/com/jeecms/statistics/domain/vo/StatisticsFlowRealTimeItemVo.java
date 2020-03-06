package com.jeecms.statistics.domain.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 实时访问明细
 * @author: chenming
 * @date:   2019年7月1日 下午2:37:13
 */
public class StatisticsFlowRealTimeItemVo {
	/** 序号 */
	private Integer id;
	/** 访问时间 */
	private Date createTime;
	/** 用户名 */
	private String areaName;
	/** 来源网址，当来源为搜索引擎或者外部链接是显示链接地址 */
	private String sourceUrl;
	/** 入口页面 */
	private String entranceUrl;
	/** 用户名 */
	private String userName;
	/** 访问ip */
	private String accessIp;
	/** 访问时长，如果访问时长为0，则为正在访问 */
	private Integer accessTime;
	/** 访问页数 */
	private Integer accessPage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
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

	public Integer getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Integer accessTime) {
		this.accessTime = accessTime;
	}

	public Integer getAccessPage() {
		return accessPage;
	}

	public void setAccessPage(Integer accessPage) {
		this.accessPage = accessPage;
	}

}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import java.math.BigDecimal;

/**
 * Base VO
 * @author: ljw
 * @date: 2019年7月3日 上午9:41:30
 */
public class BaseAccessVo {

	/** 名称 **/
	private String name;
	/** 浏览量 **/
	private Integer pv;
	/** 访客数 **/
	private Integer uv;
	/** IP数 **/
	private Integer ips;
	/** 跳出率 **/
	private BigDecimal jump;
	/** 平均访问时长 秒数**/
	private BigDecimal visitTime;
	/** 平均访问时长转时间格式**/
	private String visitTimes;
	/**是否新老客户**/
	private Boolean isNew;

	public BaseAccessVo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}

	public Integer getIps() {
		return ips;
	}

	public void setIps(Integer ips) {
		this.ips = ips;
	}

	public BigDecimal getJump() {
		return jump;
	}

	public void setJump(BigDecimal jump) {
		this.jump = jump;
	}

	public BigDecimal getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(BigDecimal visitTime) {
		this.visitTime = visitTime;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public String getVisitTimes() {
		return visitTimes;
	}

	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}

}

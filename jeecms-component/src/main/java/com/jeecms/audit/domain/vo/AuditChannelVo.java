/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain.vo;

import java.util.List;

import com.jeecms.audit.domain.AuditChannelSet;

/**
 * 栏目设置VO
 * 
 * @author: ljw
 * @date: 2019年12月17日 上午10:43:51
 */
public class AuditChannelVo {

	/** 策略ID **/
	private Integer strategyId;
	/** 策略名称 **/
	private String strategyName;
	/** 审核策略以及场景 **/
	private String strategyString;
	/** 策略ID **/
	private List<AuditChannelSet> channelSets;

	public AuditChannelVo() {
	}
	
	/**构造函数**/
	public AuditChannelVo(Integer strategyId, String strategyName) {
		this.strategyId = strategyId;
		this.strategyName = strategyName;
	}

	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public String getStrategyString() {
		return strategyString;
	}

	public void setStrategyString(String strategyString) {
		this.strategyString = strategyString;
	}

	public List<AuditChannelSet> getChannelSets() {
		return channelSets;
	}

	public void setChannelSets(List<AuditChannelSet> channelSets) {
		this.channelSets = channelSets;
	}

}
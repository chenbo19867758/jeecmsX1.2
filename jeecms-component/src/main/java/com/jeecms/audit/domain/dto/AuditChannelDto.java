/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 栏目设置DTO
 * @author: ljw
 * @date: 2019年12月17日 下午4:00:12
 */
public class AuditChannelDto {

	/** 策略ID **/
	private Integer strategyId;
	/** 栏目IDs **/
	private List<Integer> channels;

	public AuditChannelDto() {
	}

	@NotNull
	public List<Integer> getChannels() {
		return channels;
	}

	public void setChannels(List<Integer> channels) {
		this.channels = channels;
	}

	@NotNull
	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

}

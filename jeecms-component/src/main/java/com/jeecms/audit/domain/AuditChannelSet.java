/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.domain;

import java.io.Serializable;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.AbstractDomain;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

/**
 * 审核栏目设置
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
@Entity
@Table(name = "jc_audit_channel_set")
public class AuditChannelSet extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 栏目ID */
	private Integer channelId;
	/** 审核策略id */
	private Integer strategyId;
	/** 是否强制通过，true是，false否 */
	private Boolean isCompel;
	/** 状态,true开启false关闭 */
	private Boolean status;
	
	/**策略对象**/
	private AuditStrategy strategy;
	/**栏目对象**/
	private Channel channel;
	
	public AuditChannelSet() {
	}
	
	/**构造函数**/
	public AuditChannelSet(Integer channelId, Integer strategyId, Boolean isCompel, 
			Boolean status) {
		this.channelId = channelId;
		this.strategyId = strategyId;
		this.isCompel = isCompel;
		this.status = status;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_audit_channel_set", pkColumnValue = "jc_audit_channel_set",
		initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_audit_channel_set")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "channel_id", nullable = false, length = 11)
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "strategy_id", nullable = false, length = 11)
	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

	@Column(name = "is_compel", nullable = false, length = 1)
	public Boolean getIsCompel() {
		return isCompel;
	}

	public void setIsCompel(Boolean isCompel) {
		this.isCompel = isCompel;
	}

	@Column(name = "status", nullable = false, length = 1)
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "strategy_id", insertable = false, updatable = false)
	public AuditStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AuditStrategy strategy) {
		this.strategy = strategy;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", insertable = false, updatable = false)
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	/**得到栏目名称**/
	@Transient
	public String getChannelName() {
		String channelName = "";
		if (getChannel() != null) {
			channelName = getChannel().getName();
		}
		return channelName;
	}
}
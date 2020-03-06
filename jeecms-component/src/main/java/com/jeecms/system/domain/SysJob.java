package com.jeecms.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.IBaseSite;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_sys_job")
@NamedQuery(name = "SysJob.findAll", query = "SELECT s FROM SysJob s")
public class SysJob extends AbstractDomain<Integer> implements Serializable, IBaseSite {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/**
	 * 所属站点
	 */
	private Integer siteId;
	/**
	 * job类型（1-发布首页静态页  2-发布栏目静态页  3-发布内容静态页  4-定时数据库备份）
	 */
	private Integer cronType;
	/**
	 * job名字
	 */
	private String cronName;
	/**
	 * cron表达式
	 */
	private String cron;
	/**
	 * job参数
	 */
	private String params;
	/**
	 * 类路径
	 */
	private String classPath;
	/**
	 * 静态页栏目id
	 */
	private Integer[] channelIds;
	/**
	 * 静态页栏目id，多个使用逗号分隔
	 */
	private String channels;
	/**
	 * 起始时间
	 */
	private Date startTime;
	/**
	 * 执行周期类型 1-设置类型 2-cron表达式
	 */
	private Integer execCycleType;
	/**
	 * 间隔类型  1-分钟  2-小时  3-天
	 */
	private Integer intervalType;
	/**
	 * 间隔时间
	 */
	private Integer intervalNum;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态
	 */
	private Boolean status;
	/**
	 * 下次执行时间
	 */
	private Date nextExecuteDate;

	private String groupName;

	/**
	 * true 所有栏目
	 */
	private Boolean isAll;

	public SysJob() {
	}

	/***
	 * 构建最简单的Job对象
	 * @Title: buildForSimple
	 * @param groupName 任务分组名
	 * @param cronName 任务KEY
	 * @return: SysJob
	 */
	public static SysJob buildForSimple(String groupName, String cronName) {
		SysJob job = new SysJob();
		job.setGroupName(groupName);
		job.setCronName(cronName);
		return job;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_job", pkColumnValue = "jc_sys_job", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_job")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Column(name = "cron_type", nullable = false, length = 6)
	@NotNull
	public Integer getCronType() {
		return cronType;
	}

	public void setCronType(Integer cronType) {
		this.cronType = cronType;
	}

	@NotBlank
	@Length(max = 150)
	@Column(name = "cron_name", nullable = false, length = 150)
	public String getCronName() {
		return cronName;
	}

	public void setCronName(String cronName) {
		this.cronName = cronName;
	}

	@Column(name = "cron", nullable = false, length = 150)
	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	@Column(name = "params", nullable = true, length = 255)
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "class_path", nullable = false, length = 150)
	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	@Column(name = "channel_ids", nullable = true, length = 500)
	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	@Transient
	public Integer[] getChannelIds() {
		return channelIds;
	}

	public void setChannelIds(Integer[] channelIds) {
		this.channelIds = channelIds;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "start_time", nullable = false, length = 19)
	@NotNull
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "exec_cycle_type", nullable = false, length = 6)
	@NotNull
	public Integer getExecCycleType() {
		return execCycleType;
	}

	public void setExecCycleType(Integer execCycleType) {
		this.execCycleType = execCycleType;
	}

	@Column(name = "interval_type", nullable = true, length = 6)
	public Integer getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(Integer intervalType) {
		this.intervalType = intervalType;
	}

	@Column(name = "interval_num", nullable = true, length = 11)
	public Integer getIntervalNum() {
		return intervalNum;
	}

	public void setIntervalNum(Integer intervalNum) {
		this.intervalNum = intervalNum;
	}

	@Column(name = "remark", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status", nullable = false, length = 1)
	@NotNull
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Transient
	public Date getNextExecuteDate() {
		return nextExecuteDate;
	}

	public void setNextExecuteDate(Date nextExecuteDate) {
		this.nextExecuteDate = nextExecuteDate;
	}

	@Column(name = "group_name", nullable = false, length = 150)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Boolean getIsAll() {
		return isAll;
	}

	@Column(name = "is_all", nullable = false, length = 1)
	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}

	/**
	 * 新增Builder模式,可选,选择设置任意属性初始化对象
	 *
	 * @param builder
	 */
	public SysJob(Builder builder) {
		id = builder.id;
		cronName = builder.cronName;
		groupName = builder.groupName;
		cron = builder.cron;
		params = builder.params;
		remark = builder.remark;
		classPath = builder.classPath;
		status = builder.status;
	}

	public static class Builder {
		private Integer id;
		/**
		 * job名称
		 */
		private String cronName = "";
		/**
		 * 分组名
		 */
		private String groupName = "";
		/**
		 * 执行的cron
		 */
		private String cron = "";
		/**
		 * job的参数
		 */
		private String params = "";
		/**
		 * job描述信息
		 */
		private String remark = "";
		/**
		 * job的class路径
		 */
		private String classPath = "";
		/**
		 * job的执行状态,只有该值为1才会执行该Job
		 */
		private Boolean status = true;

		public Builder withId(Integer i) {
			id = i;
			return this;
		}

		public Builder withCronName(String n) {
			cronName = n;
			return this;
		}

		public Builder withGroupName(String g) {
			groupName = g;
			return this;
		}

		public Builder withCron(String c) {
			cron = c;
			return this;
		}

		public Builder withParameter(String p) {
			params = p;
			return this;
		}

		public Builder withRemark(String r) {
			remark = r;
			return this;
		}

		public Builder withClassPath(String c) {
			classPath = c;
			return this;
		}

		public Builder withStatus(Boolean s) {
			status = s;
			return this;
		}

		public SysJob newJobEntity() {
			return new SysJob(this);
		}
	}

}
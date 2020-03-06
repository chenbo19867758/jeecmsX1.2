/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.util.MyDateUtils;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 粉丝统计信息实体类
 * @author ljw
 * @date 2019年5月28日
 */
@Entity 
@Table(name = "jc_wechat_fans_statistics")
public class WechatFansStatistics extends AbstractDomain<Integer> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 全局唯一标识符 */
	private Integer id;
	/** 公众号或小程序appId */
	private String appId;
	/** 新关注用户数 */
	@Excel(name = "新关注人数", isImportField = "true", width = 11, orderNum = "2")
	private Integer newUser;
	/** 取消关注用户数 */
	@Excel(name = "取消关注人数", isImportField = "true", width = 11, orderNum = "3")
	private Integer cancelUser;
	/** 净增关注人数 */
	@Excel(name = "净增关注人数", isImportField = "true", width = 11, orderNum = "4")
	private Integer netGrowthUser;
	/** 累计关注人数 */
	@Excel(name = "累积关注人数", isImportField = "true", width = 11, orderNum = "5")
	private Integer countUser;
	@Excel(name = "统计时间", isImportField = "true", width = 26, orderNum = "1")
	/** 统计时间*/
	private Date statisticsDate; 
	/** 用户来源 */
	private Integer userSource; 
	/**公众号名称**/
	private String name;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_wechat_fans_statistics", pkColumnValue = "jc_wechat_fans_statistics", 
			initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_fans_statistics")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id")
	@NotNull
	@Length(max = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "new_user")
	public Integer getNewUser() {
		return newUser;
	}

	public void setNewUser(Integer newUser) {
		this.newUser = newUser;
	}

	@Column(name = "cancel_user")
	public Integer getCancelUser() {
		return cancelUser;
	}

	public void setCancelUser(Integer cancelUser) {
		this.cancelUser = cancelUser;
	}

	@Column(name = "net_growth_user")
	public Integer getNetGrowthUser() {
		return netGrowthUser;
	}

	public void setNetGrowthUser(Integer netGrowthUser) {
		this.netGrowthUser = netGrowthUser;
	}

	@Column(name = "count_user")
	public Integer getCountUser() {
		return countUser;
	}

	public void setCountUser(Integer countUser) {
		this.countUser = countUser;
	}

	@Column(name = "statistics_date", nullable = false)
	public Date getStatisticsDate() {
		return statisticsDate;
	}

	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	@Transient
	public String getStatisticsDates() {
		return MyDateUtils.formatDate(statisticsDate);
	}
	
	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "user_source")
	public Integer getUserSource() {
		return userSource;
	}

	public void setUserSource(Integer userSource) {
		this.userSource = userSource;
	}
	
	/** 1.日期 */
	public static final Integer ORDER_TYPE_DATE = 1;
	/** 2.新关注人数 */
	public static final Integer ORDER_TYPE_NEW = 2;
	/** 3.取消关注人数 */
	public static final Integer ORDER_TYPE_QUIT = 3;
	/** 4.净关注人数 */
	public static final Integer ORDER_TYPE_NORMAL = 4;
	/** 5.累计关注人数 */
	public static final Integer ORDER_TYPE_SUM = 5;
}

/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain.vo;

import java.util.Date;

import javax.persistence.Transient;

import com.jeecms.common.util.MyDateUtils;

/**
 * 统计VO
 * 
 * @author: ljw
 * @date: 2019年6月12日 上午10:04:49
 */
public class WechatStatisticsVO {

	/** 新关注用户数 */
	private Integer newUser;
	/** 取消关注用户数 */
	private Integer cancelUser;
	/** 净增关注人数 */
	private Integer netGrowthUser;
	/** 累计关注人数 */
	private Integer countUser;
	/** 统计时间 */
	private Date statisticsDate;

	public WechatStatisticsVO() {
	}

	public Integer getNewUser() {
		return newUser;
	}

	public void setNewUser(Integer newUser) {
		this.newUser = newUser;
	}

	public Integer getCancelUser() {
		return cancelUser;
	}

	public void setCancelUser(Integer cancelUser) {
		this.cancelUser = cancelUser;
	}

	public Integer getNetGrowthUser() {
		return netGrowthUser;
	}

	public void setNetGrowthUser(Integer netGrowthUser) {
		this.netGrowthUser = netGrowthUser;
	}

	public Integer getCountUser() {
		return countUser;
	}

	public void setCountUser(Integer countUser) {
		this.countUser = countUser;
	}

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

}

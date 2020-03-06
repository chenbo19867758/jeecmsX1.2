/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.userstatistics;

/**
 * 获取用户增减数据 （最大时间跨度7） 获取累计用户数据 （最大时间跨度7）
 * @link https://api.weixin.qq.com/datacube/getusersummary?access_token=ACCESS_TOKEN
 * @link https://api.weixin.qq.com/datacube/getusercumulate?access_token=ACCESS_TOKEN
 * @author ljw
 * @date 2019年5月30日
 */
public class UserStatistics {

	/**
	 * 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 * （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 */
	private String beginDate;

	/** 获取数据的结束日期，end_date允许设置的最大值为昨日 */
	private String endDate;

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}

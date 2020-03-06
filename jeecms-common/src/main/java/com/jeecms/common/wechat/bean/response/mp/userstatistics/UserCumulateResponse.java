/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.response.mp.userstatistics;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 获取累计用户数据(最大时间跨度7)
 * @author ljw
 * @date 2019年05月30日
 */
public class UserCumulateResponse {

	private List<UserCountResult> list;

	public List<UserCountResult> getList() {
		return list;
	}

	public void setList(List<UserCountResult> list) {
		this.list = list;
	}

	public static class UserCountResult {

		/** 数据的日期 */
		@XStreamAlias("ref_date")
		private String refDate;

		/** 总用户量 */
		@XStreamAlias("cumulate_user")
		private Integer cumulateUser;

		public String getRefDate() {
			return refDate;
		}

		public void setRefDate(String refDate) {
			this.refDate = refDate;
		}

		public Integer getCumulateUser() {
			return cumulateUser;
		}

		public void setCumulateUser(Integer cumulateUser) {
			this.cumulateUser = cumulateUser;
		}

	}
}

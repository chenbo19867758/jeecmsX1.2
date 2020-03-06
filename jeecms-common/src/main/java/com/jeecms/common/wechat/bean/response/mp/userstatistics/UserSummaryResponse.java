/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.response.mp.userstatistics;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 获取用户增减数据(最大时间跨度7)
 * @author ljw
 * @date 2018年7月31日
 */
public class UserSummaryResponse {

	private List<UserSummaryResult> list;

	public List<UserSummaryResult> getList() {
		return list;
	}

	public void setList(List<UserSummaryResult> list) {
		this.list = list;
	}

	public static class UserSummaryResult {

		/** 数据的日期 */
		@XStreamAlias("ref_date")
		private String refDate;

		/**
		 * 用户的渠道，数值代表的含义如下： 0代表其他合计 1代表公众号搜索 17代表名片分享 30代表扫描二维码 43代表图文页右上角菜单
		 * 51代表支付后关注（在支付完成页） 57代表图文页内公众号名称 75代表公众号文章广告 78代表朋友圈广告
		 */
		@XStreamAlias("user_source")
		private Integer userSource;

		/** 新增的用户数量 */
		@XStreamAlias("new_user")
		private Integer newUser;

		/** 取消关注的用户数量 */
		@XStreamAlias("cancel_user")
		private Integer cancelUser;

		public String getRefDate() {
			return refDate;
		}

		public void setRefDate(String refDate) {
			this.refDate = refDate;
		}

		public Integer getUserSource() {
			return userSource;
		}

		public void setUserSource(Integer userSource) {
			this.userSource = userSource;
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

	}

}

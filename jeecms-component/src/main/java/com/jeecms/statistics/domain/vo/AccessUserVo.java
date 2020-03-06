/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import java.math.BigDecimal;

/**
 * 新老客户VO
 * 
 * @author: ljw
 * @date: 2019年7月2日 上午9:09:40
 */
public class AccessUserVo extends BaseAccessVo {
	
	/**平均访问页数**/
	private BigDecimal visitPage;
	
	public AccessUserVo() {}
	
	public BigDecimal getVisitPage() {
		return visitPage;
	}

	public void setVisitPage(BigDecimal visitPage) {
		this.visitPage = visitPage;
	}

	public class DeviceVo {

		/** 名称 **/
		private String type;
		/** 次数 **/
		private Integer value;

		public DeviceVo() {
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}
}

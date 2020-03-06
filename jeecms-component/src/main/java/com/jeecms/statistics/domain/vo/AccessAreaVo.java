/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.statistics.domain.vo;

import java.util.List;

/**   
 *  地域分布VO
 * @author: ljw
 * @date:   2019年7月1日 上午9:18:53     
 */
public class AccessAreaVo extends BaseAccessVo {

	/**城市数据**/
	private List<BaseAccessVo> cityList;
	/**PV数据列表**/
	private List<StatisticsVisitorVo> pvList;
	/**UV数据列表**/
	private List<StatisticsVisitorVo> uvList;
	/**IP数据列表**/
	private List<StatisticsVisitorVo> ipsList;
	
	public AccessAreaVo() {
		super();
	}

	public List<StatisticsVisitorVo> getPvList() {
		return pvList;
	}

	public void setPvList(List<StatisticsVisitorVo> pvList) {
		this.pvList = pvList;
	}

	public List<StatisticsVisitorVo> getUvList() {
		return uvList;
	}

	public void setUvList(List<StatisticsVisitorVo> uvList) {
		this.uvList = uvList;
	}

	public List<StatisticsVisitorVo> getIpsList() {
		return ipsList;
	}

	public void setIpsList(List<StatisticsVisitorVo> ipsList) {
		this.ipsList = ipsList;
	}

	public List<BaseAccessVo> getCityList() {
		return cityList;
	}

	public void setCityList(List<BaseAccessVo> cityList) {
		this.cityList = cityList;
	}
	
}

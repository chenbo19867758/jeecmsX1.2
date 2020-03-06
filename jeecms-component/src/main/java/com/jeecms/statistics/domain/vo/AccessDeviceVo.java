/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.domain.vo;

import java.util.List;

/**
 * 设备VO
 * 
 * @author: ljw
 * @date: 2019年7月2日 上午9:09:40
 */
public class AccessDeviceVo extends BaseAccessVo {

	/** PV数据列表 **/
	private List<DeviceVo> pvList;
	/** UV数据列表 **/
	private List<DeviceVo> uvList;
	/** IP数据列表 **/
	private List<DeviceVo> ipsList;
	/**设备数据**/
	private List<BaseAccessVo> deviceList;
	
	public AccessDeviceVo() {}
	
	public List<DeviceVo> getPvList() {
		return pvList;
	}

	public void setPvList(List<DeviceVo> pvList) {
		this.pvList = pvList;
	}

	public List<DeviceVo> getUvList() {
		return uvList;
	}

	public void setUvList(List<DeviceVo> uvList) {
		this.uvList = uvList;
	}

	public List<DeviceVo> getIpsList() {
		return ipsList;
	}

	public void setIpsList(List<DeviceVo> ipsList) {
		this.ipsList = ipsList;
	}

	public List<BaseAccessVo> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<BaseAccessVo> deviceList) {
		this.deviceList = deviceList;
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

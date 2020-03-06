package com.jeecms.common.wechat.bean.request.mp.menu.incident;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:40:34
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ScancodePushEvent extends BaseEvent{
	
	/** 扫描信息 */
	@XStreamAlias("ScanCodeInfo")
	private String scanCodeInfo;
	
	/** 扫描类型，一般是qrcode */
	@XStreamAlias("ScanType")
	private String scanType;
	
	/** 扫描结果，即二维码对应的字符串信息 */
	@XStreamAlias("ScanResult")
	private String scanResult;

	public String getScanCodeInfo() {
		return scanCodeInfo;
	}

	public void setScanCodeInfo(String scanCodeInfo) {
		this.scanCodeInfo = scanCodeInfo;
	}

	public String getScanType() {
		return scanType;
	}

	public void setScanType(String scanType) {
		this.scanType = scanType;
	}

	public String getScanResult() {
		return scanResult;
	}

	public void setScanResult(String scanResult) {
		this.scanResult = scanResult;
	}

}

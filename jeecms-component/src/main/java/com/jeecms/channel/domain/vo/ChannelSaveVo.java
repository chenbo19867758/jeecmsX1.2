package com.jeecms.channel.domain.vo;

/**
 * 栏目新增显示字段扩展VO
 * @author: chenming
 * @date:   2019年5月5日 下午1:46:00
 */
public class ChannelSaveVo {
	/** 是否显示在栏目循环列表 */
	private Boolean display;
	/** 栏目页模板 */
	private String tplPc;
	/** 手机栏目页模板 */
	private String tplMobile;

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public String getTplPc() {
		return tplPc;
	}

	public void setTplPc(String tplPc) {
		this.tplPc = tplPc;
	}

	public String getTplMobile() {
		return tplMobile;
	}

	public void setTplMobile(String tplMobile) {
		this.tplMobile = tplMobile;
	}

}

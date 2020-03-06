package com.jeecms.common.wechat.bean.request.applet;

import com.jeecms.common.wechat.bean.request.applet.common.Colour;

/**
 * 小程序获取二维码request请求对象
 * @author: chenming
 * @date:   2019年9月2日 上午9:29:36
 */
public class GetQrcodeRequest {
	/** 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）*/
	private String scene;
	/** 必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面*/
	private String page;
	/** 二维码的宽度，单位 px，最小 280px，最大 1280px*/
	private Integer width;
	/** 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false*/
	private Boolean autoColor;
	/** auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示*/
	private Colour lineColor;
	/** 是否需要透明底色，为 true 时，生成透明底色的小程序*/
	private Boolean isHyaline;

	public GetQrcodeRequest(String scene, String page, Integer width, Boolean autoColor, Colour lineColor,
			Boolean isHyaline) {
		super();
		this.scene = scene;
		this.page = page;
		this.width = width;
		this.autoColor = autoColor;
		this.lineColor = lineColor;
		this.isHyaline = isHyaline;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Boolean getAutoColor() {
		return autoColor;
	}

	public void setAutoColor(Boolean autoColor) {
		this.autoColor = autoColor;
	}

	public Colour getLineColor() {
		return lineColor;
	}

	public void setLineColor(Colour lineColor) {
		this.lineColor = lineColor;
	}

	public Boolean getIsHyaline() {
		return isHyaline;
	}

	public void setIsHyaline(Boolean isHyaline) {
		this.isHyaline = isHyaline;
	}

}

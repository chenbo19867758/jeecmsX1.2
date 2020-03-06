package com.jeecms.common.wechat.bean.request.applet.common;

/**
 * 颜色对象(rgb设置颜色，例如{"r":"xxx","g":"xxx","b":"xxx"} 十进制表示)
 * 
 * @author: chenming
 * @date: 2019年9月2日 上午9:24:43
 */
public class Colour {

	private Integer r;

	private Integer g;

	private Integer b;

	public Colour(Integer r, Integer g, Integer b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Integer getR() {
		return r;
	}

	public void setR(Integer r) {
		this.r = r;
	}

	public Integer getG() {
		return g;
	}

	public void setG(Integer g) {
		this.g = g;
	}

	public Integer getB() {
		return b;
	}

	public void setB(Integer b) {
		this.b = b;
	}

}

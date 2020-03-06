/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import java.math.BigDecimal;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/1 10:52
 */
public class QuestionnairePieVo {

	/**
	 * 按时显示
	 */
	public static final int HOUR = 1;
	/**
	 * 按天显示
	 */
	public static final int DAY = 2;
	/**
	 * 按周显示
	 */
	public static final int WEEK = 3;
	/**
	 * 按月统计
	 */
	public static final int MOUTH = 4;

	/**
	 * a : 事例一
	 * count : 40
	 */

	private String item;
	private int count;
	/**
	 * 选项id
	 */
	private String optionId;
	/**
	 * 比率
	 */
	private BigDecimal rate;

	public QuestionnairePieVo() {
		super();
	}

	public QuestionnairePieVo(String item, int count, String optionId) {
		this.item = item;
		this.count = count;
		this.optionId = optionId;
	}

	public QuestionnairePieVo(String item, int count, BigDecimal rate) {
		this.item = item;
		this.count = count;
		this.rate = rate;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
}

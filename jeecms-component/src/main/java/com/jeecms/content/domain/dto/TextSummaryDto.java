package com.jeecms.content.domain.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 自动摘要dto
 * @author: chenming
 * @date:   2019年7月15日 上午10:18:49
 */
public class TextSummaryDto {
	/** 字符串主体 */
	private String text;
	/** 摘要的关键句子需要几个 */
	private Integer keySentence;
	/** 返回的字符串的大小*/
	private Integer size;

	@NotBlank
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@NotNull
	@Min(1)
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@NotNull
	public Integer getKeySentence() {
		return keySentence;
	}

	public void setKeySentence(Integer keySentence) {
		this.keySentence = keySentence;
	}

}

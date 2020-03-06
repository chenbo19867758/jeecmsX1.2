/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增审核策略Dto
 *
 * @author xiaohui
 * @version 1.2
 * @date 2019/12/18 9:48
 */
public class AuditStrategySaveDto {


	/*
	 * name : 策略名称
	 * isText : true
	 * textScenes : [1,2]
	 * isPicture : true
	 * pictureScenes : [1,2]
	 * status : true
	 */

	/**
	 * 策略名称
	 */
	private String name;
	/**
	 * true 开启文本审核， false则反之
	 */
	private Boolean isText = true;
	/**
	 * true 开启图片审核， false则反之
	 */
	private Boolean isPicture = true;
	/**
	 * true 开启策略， false 关闭策略
	 */
	private Boolean status;
	/**
	 * 文本审核场景
	 */
	private List<Integer> textScenes;
	/**
	 * 图片审核场景
	 */
	private List<Integer> pictureScenes;

	public AuditStrategySaveDto() {
		super();
	}

	public AuditStrategySaveDto(String name, Boolean isText, Boolean isPicture, Boolean status,
								List<Integer> textScenes, List<Integer> pictureScenes) {
		this.name = name;
		this.isText = isText;
		this.isPicture = isPicture;
		this.status = status;
		this.textScenes = textScenes;
		this.pictureScenes = pictureScenes;
	}

	@NotBlank
	@Length(max = 25)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Boolean getIsText() {
		return isText;
	}

	public void setIsText(Boolean isText) {
		this.isText = isText;
	}

	public Boolean getIsPicture() {
		return isPicture;
	}

	public void setIsPicture(Boolean isPicture) {
		this.isPicture = isPicture;
	}

	@NotNull
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<Integer> getTextScenes() {
		return textScenes;
	}

	public void setTextScenes(List<Integer> textScenes) {
		this.textScenes = textScenes;
	}

	public List<Integer> getPictureScenes() {
		return pictureScenes;
	}

	public void setPictureScenes(List<Integer> pictureScenes) {
		this.pictureScenes = pictureScenes;
	}
}

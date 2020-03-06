/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 模型DTO
 * 
 * @author: ljw
 * @date: 2019年12月18日 上午10:22:47
 */
public class AuditModelDto {

	/**模型设置ID**/
	private Integer id;
	/** 模型ID **/
	private Integer modelId;
	/** 站点ID **/
	private Integer siteId;
	/** 模型字段IDs **/
	private List<Integer> items;

	public AuditModelDto() {
	}

	@NotNull
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@NotNull
	@NotEmpty
	public List<Integer> getItems() {
		return items;
	}

	public void setItems(List<Integer> items) {
		this.items = items;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
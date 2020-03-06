/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain.vo;

/**
 * 模型设置VO
 * 
 * @author: ljw
 * @date: 2019年12月18日 上午9:39:17
 */
public class AuditModelVo {

	/** 模型ID **/
	private Integer modelId;
	/** 模型名称 **/
	private String modelName;
	/** 是否置灰 **/
	private Boolean gray;

	public AuditModelVo() {
	}
	
	/**构造函数**/
	public AuditModelVo(Integer modelId, String modelName) {
		this.modelId = modelId;
		this.modelName = modelName;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Boolean getGray() {
		return gray;
	}

	public void setGray(Boolean gray) {
		this.gray = gray;
	}

}

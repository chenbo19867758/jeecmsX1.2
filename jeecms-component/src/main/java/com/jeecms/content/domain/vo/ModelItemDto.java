/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.domain.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;


/**   
 * 模型字段提交dto
 * @author: wangqq
 * @date:   2019年6月4日 下午4:27:56     
 */
public class ModelItemDto implements Serializable {

		private static final long serialVersionUID = -6265067911977156831L;
		/**模型ID*/
		@NotBlank
		private Integer modelId;
		/**模型中启用字段，json对象格式字符串*/
		@NotBlank
		private String enableJson;
		/**模型中未启用字段，jsong数组格式字符串*/
		private String unEnableJson;
		public Integer getModelId() {
			return modelId;
		}
		public void setModelId(Integer modelId) {
			this.modelId = modelId;
		}
		public String getEnableJson() {
			return enableJson;
		}
		public void setEnableJson(String enableJson) {
			this.enableJson = enableJson;
		}
		public String getUnEnableJson() {
			return unEnableJson;
		}
		public void setUnEnableJson(String unEnableJson) {
			this.unEnableJson = unEnableJson;
		}
        
		
}

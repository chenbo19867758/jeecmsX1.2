/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.content.domain.dto;

import javax.validation.constraints.NotNull;

/**   
 * 模型dto
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年4月17日 下午6:02:37     
 */
public class CmsModelDto {

    /**模型Id*/
    private Integer id;
  
    /** 是否启用 */
    private  Boolean isEnable;

    /**模型名称*/
    private String modelName;
    
    @NotNull
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    
}

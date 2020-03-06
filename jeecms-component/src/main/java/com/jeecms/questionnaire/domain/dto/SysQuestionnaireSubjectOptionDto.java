/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/22 10:45
 */
public class SysQuestionnaireSubjectOptionDto {

    /**
     * 选项
     */
    private String name;
    /**
     * 图片
     */
    private String pic;
    /**
     * 是否默认
     */
    private Boolean isDefault;
    /**
     * 是否必填
     */
    private Boolean isRequired;
    /**
     * 排序值
     */
    private Integer sortNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
}

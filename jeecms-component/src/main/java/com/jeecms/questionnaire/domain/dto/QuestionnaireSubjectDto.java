/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * 问卷题目Dto
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/29 11:29
 */
public class QuestionnaireSubjectDto {

    /**
     * id : 1
     * dragable : true
     * editor : VRadioEditor
     * groupIndex : 8
     * index : 2
     * isAnswer : false
     * isCustom : true
     * option : {"column":1,"fileNum":"","fileNumLimit":false,"fileSize":"","fileSizeLimit":false,"fileTypeLimit":false,"fileTypeSet":1,"fileTypes":"","fileUnit":"KB","inputHeight":"40","inputLimit":false,"inputNum":0,"inputNumLimit":false,"inputType":"","inputWidth":"100","isRadio":1,"options":[{"isDefault":false,"isRequired":false,"name":"","pic":"","sortNum":1}]}
     * placeholder : 
     * preview : VRadioPreview
     * prompt : 
     * name :
     * type : 1
     * value : {"defaultValue":"","name":"details"}
     */

    private Integer id;

    /**
     * 是否可拖动
     */
    private Boolean dragable;
    /**
     * 编辑类型
     */
    private String editor;
    /**
     * 题目左侧排序
     */
    private Integer groupIndex;
    /**
     * 前端使用
     */
    private String icon;
    /**
     * index
     */
    private Integer index;
    /**
     * 是否必答
     */
    private Boolean isAnswer = false;
    /**
     * 是否自定义字段
     */
    private Boolean isCustom;
    /**
     * 选项
     */
    private QuestionnaireOptionDto option;
    /**
     * 题目类型
     */
    private String placeholder;
    /**
     * 预览类型
     */
    private String preview;
    /**
     * 提示
     */
    private String prompt;
    /**
     * 题目标题
     */
    private String title;
    /**
     * 题目类型
     */
    private Short type;
    /**
     *
     */
    private ValueBean value;

    public QuestionnaireSubjectDto() {
        super();
    }

    public QuestionnaireSubjectDto(Integer id, Boolean dragable, String editor, Integer groupIndex, String icon, Integer index, Boolean isAnswer, Boolean isCustom, QuestionnaireOptionDto option, String placeholder, String preview, String prompt, String title, Short type, ValueBean value) {
        this.id = id;
        this.dragable = dragable;
        this.editor = editor;
        this.groupIndex = groupIndex;
        this.icon = icon;
        this.index = index;
        this.isAnswer = isAnswer;
        this.isCustom = isCustom;
        this.option = option;
        this.placeholder = placeholder;
        this.preview = preview;
        this.prompt = prompt;
        this.title = title;
        this.type = type;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDragable() {
        return dragable;
    }

    public void setDragable(Boolean dragable) {
        this.dragable = dragable;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getIsAnswer() {
        return isAnswer == null ? false : isAnswer;
    }

    public void setIsAnswer(Boolean isAnswer) {
        this.isAnswer = isAnswer;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

    public QuestionnaireOptionDto getOption() {
        return option;
    }

    public void setOption(QuestionnaireOptionDto option) {
        this.option = option;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @NotBlank
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * defaultValue : 
         * name : details
         */

        /**
         * 默认值：或数组
         */
        private String defaultValue;
        /**
         * 字段名称
         */
        private String name;

        public ValueBean() {
            super();
        }

        public ValueBean(String defaultValue, String name) {
            this.defaultValue = defaultValue;
            this.name = name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

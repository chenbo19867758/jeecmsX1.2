/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

import com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/22 10:44
 */
public class SysQuestionnaireSubjectDto {

    private Integer id;
    /**
     * 题目标题
     */
    private String title;
    /**
     * 题目类型（1单选2多选3填空4下拉5级联6附件）
     */
    private Short type;
    /**
     * 是否必答
     */
    private Boolean isAnswer;
    /**
     * 提示
     */
    private String prompt;
    /**
     * 每行列数
     */
    private Integer column;
    /**
     * 是否开启限制条件
     */
    private Boolean isLimit;
    /**
     * 限制条件
     */
    private Integer limitCondition;
    /**
     * 级联题选项json
     */
    private String cascadeOption;
    /**
     * 字数限制
     */
    private String wordLimit;
    /**
     * 输入框高度
     */
    private Integer inputHigh;
    /**
     * 输入框宽度
     */
    private Integer inputWidth;
    /**
     * 是否开启文件大小限制
     */
    private Boolean isFileSize;
    /**
     * 文件大小限制
     */
    private Integer fileSizeLimit;
    /**
     * 文件大小限制单位
     */
    private Integer fileSizeLimitUnit;
    /**
     * 是否开启文件数量限制
     */
    private Boolean isFileCount;
    /**
     * 文件数量限制
     */
    private Integer fileCountLimit;
    /**
     * 是否开启文件类型限制
     */
    private Boolean isFileType;
    /**
     * 设置允许true 设置禁止 false
     */
    private Boolean isAllowFile;
    /**
     * 限制类型，多个用逗号隔开
     */
    private String fileTypeLimit;

    /**
     * true 单选 false 多选
     */
    private Boolean isRadio;

    private List<SysQuestionnaireSubjectOption> options;

    public SysQuestionnaireSubjectDto() {
        super();
    }

    public SysQuestionnaireSubjectDto(String title, Short type, Boolean isAnswer, String prompt, Integer column,
                                      Boolean isLimit, Integer limitCondition, String cascadeOption, String wordLimit,
                                      Integer inputHigh, Integer inputWidth, Boolean isFileSize, Integer fileSizeLimit,
                                      Integer fileSizeLimitUnit, Boolean isFileCount, Integer fileCountLimit, Boolean isFileType,
                                      Boolean isAllowFile, String fileTypeLimit, Boolean isRadio, List<SysQuestionnaireSubjectOption> options) {
        this.title = title;
        this.type = type;
        this.isAnswer = isAnswer;
        this.prompt = prompt;
        this.column = column;
        this.isLimit = isLimit;
        this.limitCondition = limitCondition;
        this.cascadeOption = cascadeOption;
        this.wordLimit = wordLimit;
        this.inputHigh = inputHigh;
        this.inputWidth = inputWidth;
        this.isFileSize = isFileSize;
        this.fileSizeLimit = fileSizeLimit;
        this.fileSizeLimitUnit = fileSizeLimitUnit;
        this.isFileCount = isFileCount;
        this.fileCountLimit = fileCountLimit;
        this.isFileType = isFileType;
        this.isAllowFile = isAllowFile;
        this.fileTypeLimit = fileTypeLimit;
        this.isRadio = isRadio;
        this.options = options;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Boolean getAnswer() {
        return isAnswer;
    }

    public void setAnswer(Boolean answer) {
        isAnswer = answer;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Boolean getLimit() {
        return isLimit;
    }

    public void setLimit(Boolean limit) {
        isLimit = limit;
    }

    public Integer getLimitCondition() {
        return limitCondition;
    }

    public void setLimitCondition(Integer limitCondition) {
        this.limitCondition = limitCondition;
    }

    public String getCascadeOption() {
        return cascadeOption;
    }

    public void setCascadeOption(String cascadeOption) {
        this.cascadeOption = cascadeOption;
    }

    public String getWordLimit() {
        return wordLimit;
    }

    public void setWordLimit(String wordLimit) {
        this.wordLimit = wordLimit;
    }

    public Integer getInputHigh() {
        return inputHigh;
    }

    public void setInputHigh(Integer inputHigh) {
        this.inputHigh = inputHigh;
    }

    public Integer getInputWidth() {
        return inputWidth;
    }

    public void setInputWidth(Integer inputWidth) {
        this.inputWidth = inputWidth;
    }

    public Boolean getFileSize() {
        return isFileSize;
    }

    public void setFileSize(Boolean fileSize) {
        isFileSize = fileSize;
    }

    public Integer getFileSizeLimit() {
        return fileSizeLimit;
    }

    public void setFileSizeLimit(Integer fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
    }

    public Integer getFileSizeLimitUnit() {
        return fileSizeLimitUnit;
    }

    public void setFileSizeLimitUnit(Integer fileSizeLimitUnit) {
        this.fileSizeLimitUnit = fileSizeLimitUnit;
    }

    public Boolean getFileCount() {
        return isFileCount;
    }

    public void setFileCount(Boolean fileCount) {
        isFileCount = fileCount;
    }

    public Integer getFileCountLimit() {
        return fileCountLimit;
    }

    public void setFileCountLimit(Integer fileCountLimit) {
        this.fileCountLimit = fileCountLimit;
    }

    public Boolean getFileType() {
        return isFileType;
    }

    public void setFileType(Boolean fileType) {
        isFileType = fileType;
    }

    public Boolean getAllowFile() {
        return isAllowFile;
    }

    public void setAllowFile(Boolean isAllowFile) {
        this.isAllowFile = isAllowFile;
    }

    public String getFileTypeLimit() {
        return fileTypeLimit;
    }

    public void setFileTypeLimit(String fileTypeLimit) {
        this.fileTypeLimit = fileTypeLimit;
    }

    public Boolean getRadio() {
        return isRadio;
    }

    public void setRadio(Boolean radio) {
        isRadio = radio;
    }

    public List<SysQuestionnaireSubjectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SysQuestionnaireSubjectOption> options) {
        this.options = options;
    }
}

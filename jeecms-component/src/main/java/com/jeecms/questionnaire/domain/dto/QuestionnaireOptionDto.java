/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

import java.util.List;

/**
 * 问卷题目规则及选项Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/29 14:12
 */
public class QuestionnaireOptionDto {

    /**
     * column : 1
     * fileNum :
     * fileNumLimit : false
     * fileSize :
     * fileSizeLimit : false
     * fileTypeLimit : false
     * fileTypeSet : 1
     * fileTypes :
     * fileUnit : KB
     * inputHeight : 40
     * inputLimit : false
     * inputNum : 0
     * inputNumLimit : false
     * inputType :
     * inputWidth : 100
     * isRadio : 1
     * options : [{"isDefault":false,"isRequired":false,"name":"","pic":"","sortNum":1}]
     */

    /**
     * 每行列数
     */
    private Integer column;
    /**
     * 附件数量
     */
    private Integer fileNum;
    /**
     * 附件数量限制
     */
    private Boolean fileNumLimit;
    /**
     * 附件大小
     */
    private Integer fileSize;
    /**
     * 附件大小限制
     */
    private Boolean fileSizeLimit;
    /**
     * 附件类型限制
     */
    private Boolean fileTypeLimit;
    /**
     * 附件类型（0禁止 1允许）
     */
    private Integer fileTypeSet;
    /**
     * 附件类型
     */
    private String fileTypes;
    /**
     * 附件大小单位
     */
    private String fileUnit;
    /**
     * 输入框高度
     */
    private Integer inputHeight;
    /**
     * 是否限制输入类型
     */
    private Boolean inputLimit;
    /**
     * 限制字数
     */
    private Integer inputNum;
    /**
     * 是否限制输入字数
     */
    private Boolean inputNumLimit;
    /**
     * 输入限制
     */
    private Integer inputType;
    /**
     * 输入框宽度
     */
    private Integer inputWidth;
    /**
     * 是否单选-下拉题
     */
    private Integer isRadio;
    /**
     * 选项
     */
    private List<OptionsBean> options;

    public QuestionnaireOptionDto() {
        super();
    }

    public QuestionnaireOptionDto(Integer column, Integer fileNum, Boolean fileNumLimit, Integer fileSize, Boolean fileSizeLimit, Boolean fileTypeLimit, Integer fileTypeSet, String fileTypes, String fileUnit, Integer inputHeight, Boolean inputLimit, Integer inputNum, Boolean inputNumLimit, Integer inputType, Integer inputWidth, Integer isRadio, List<OptionsBean> options) {
        this.column = column;
        this.fileNum = fileNum;
        this.fileNumLimit = fileNumLimit;
        this.fileSize = fileSize;
        this.fileSizeLimit = fileSizeLimit;
        this.fileTypeLimit = fileTypeLimit;
        this.fileTypeSet = fileTypeSet;
        this.fileTypes = fileTypes;
        this.fileUnit = fileUnit;
        this.inputHeight = inputHeight;
        this.inputLimit = inputLimit;
        this.inputNum = inputNum;
        this.inputNumLimit = inputNumLimit;
        this.inputType = inputType;
        this.inputWidth = inputWidth;
        this.isRadio = isRadio;
        this.options = options;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Integer getFileNum() {
        return fileNum;
    }

    public void setFileNum(Integer fileNum) {
        this.fileNum = fileNum;
    }

    public Boolean getFileNumLimit() {
        return fileNumLimit;
    }

    public void setFileNumLimit(Boolean fileNumLimit) {
        this.fileNumLimit = fileNumLimit;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getFileSizeLimit() {
        return fileSizeLimit;
    }

    public void setFileSizeLimit(Boolean fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
    }

    public Boolean getFileTypeLimit() {
        return fileTypeLimit;
    }

    public void setFileTypeLimit(Boolean fileTypeLimit) {
        this.fileTypeLimit = fileTypeLimit;
    }

    public Integer getFileTypeSet() {
        return fileTypeSet;
    }

    public void setFileTypeSet(Integer fileTypeSet) {
        this.fileTypeSet = fileTypeSet;
    }

    public String getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(String fileTypes) {
        this.fileTypes = fileTypes;
    }

    public String getFileUnit() {
        return fileUnit;
    }

    public void setFileUnit(String fileUnit) {
        this.fileUnit = fileUnit;
    }

    public Integer getInputHeight() {
        return inputHeight;
    }

    public void setInputHeight(Integer inputHeight) {
        this.inputHeight = inputHeight;
    }

    public Boolean getInputLimit() {
        return inputLimit;
    }

    public void setInputLimit(Boolean inputLimit) {
        this.inputLimit = inputLimit;
    }

    public Integer getInputNum() {
        return inputNum;
    }

    public void setInputNum(Integer inputNum) {
        this.inputNum = inputNum;
    }

    public Boolean getInputNumLimit() {
        return inputNumLimit;
    }

    public void setInputNumLimit(Boolean inputNumLimit) {
        this.inputNumLimit = inputNumLimit;
    }

    public Integer getInputType() {
        return inputType;
    }

    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    public Integer getInputWidth() {
        return inputWidth;
    }

    public void setInputWidth(Integer inputWidth) {
        this.inputWidth = inputWidth;
    }

    public Integer getIsRadio() {
        return isRadio;
    }

    public void setIsRadio(Integer isRadio) {
        this.isRadio = isRadio;
    }

    public List<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsBean> options) {
        this.options = options;
    }

    public static class OptionsBean {
        /**
         * isDefault : false
         * isRequired : false
         * name :
         * pic :
         * sortNum : 1
         */


        private Integer id;
        /**
         * 是否默认
         */
        private Boolean isDefault;
        /**
         * 是否必填
         */
        private Boolean isRequired;
        /**
         * 是否可自己填写
         */
        private Boolean isEemty;
        /**
         * 单个选项
         */
        private String name;
        /**
         * 图片id
         */
        private Integer pic;
        /**
         * 图片地址
         */
        private String picUrl;
        /**
         * 排序
         */
        private Integer sortNum;
        /**
         * 子选项
         */
        private OptionsBean[] children;

        public OptionsBean() {
            super();
        }

        public OptionsBean(Integer id, Boolean isDefault, Boolean isRequired, String name, Integer pic, Integer sortNum) {
            this.id = id;
            this.isDefault = isDefault;
            this.isRequired = isRequired;
            this.name = name;
            this.pic = pic;
            this.sortNum = sortNum;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(Boolean isDefault) {
            this.isDefault = isDefault;
        }

        public Boolean getIsRequired() {
            return isRequired;
        }

        public void setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
        }

        public Boolean getIsEemty() {
            return isEemty;
        }

        public void setIsEemty(Boolean isEemty) {
            this.isEemty = isEemty;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPic() {
            return pic;
        }

        public void setPic(Integer pic) {
            this.pic = pic;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public Integer getSortNum() {
            return sortNum;
        }

        public void setSortNum(Integer sortNum) {
            this.sortNum = sortNum;
        }

        public OptionsBean[] getChildren() {
            return children;
        }

        public void setChildren(OptionsBean[] children) {
            this.children = children;
        }
    }

}

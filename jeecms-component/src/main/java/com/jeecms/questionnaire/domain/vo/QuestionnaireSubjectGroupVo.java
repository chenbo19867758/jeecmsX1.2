/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 问卷题目分组
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/31 9:56
 */
public class QuestionnaireSubjectGroupVo {
    /**
     * 题目id
     */
    private Integer subjectId;
    /**
     * 标题
     */
    private String title;
    /**
     * 题目类型
     */
    private Short type;
    /**
     * 票数
     */
    private Integer number;
    /**
     * 选项
     */
    private List<Option> options;

    private Boolean isFile;

    private Integer index;

    public QuestionnaireSubjectGroupVo() {
        super();
    }

    public QuestionnaireSubjectGroupVo(Integer subjectId, String title, Short type, Integer number, Integer index, List<Option> options, Boolean isFile) {
        this.subjectId = subjectId;
        this.title = title;
        this.type = type;
        this.number = number;
        this.index = index;
        this.options = options;
        this.isFile = isFile;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public static class Option {
        /**
         * 选项id
         */
        private Integer optionId;
        /**
         * 选项值
         */
        private String title;
        /**
         * 选项票数
         */
        private Long number;
        /**
         * 选项占比
         */
        private BigDecimal rate;

        public Integer getOptionId() {
            return optionId;
        }

        public void setOptionId(Integer optionId) {
            this.optionId = optionId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getNumber() {
            return number;
        }

        public void setNumber(Long number) {
            this.number = number;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(Boolean file) {
        isFile = file;
    }
}

/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Entity
@Table(name = "jc_sys_vote_subject_option")
public class SysQuestionnaireSubjectOption extends AbstractDomain<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 题目id
     */
    private Integer questionnaireSubjectId;
    /**
     * 选项
     */
    private String name;
    /**
     * 图片
     */
    private Integer picId;
    private ResourcesSpaceData pic;
    /**
     * 是否默认
     */
    private Boolean isDefault;
    /**
     * 是否必填
     */
    private Boolean isRequired;

    /**
     * 是否可自定义
     */
    private Boolean isOther;
    /**
     * 排序值
     */
    private Integer sortNum;
    /**
     * 题目
     */
    private SysQuestionnaireSubject questionnaireSubject;

    public SysQuestionnaireSubjectOption() {
    }

    public SysQuestionnaireSubjectOption(Integer questionnaireSubjectId, String name, Integer picId, Boolean isDefault, Boolean isRequired, Boolean isOther, Integer sortNum, SysQuestionnaireSubject questionnaireSubject) {
        this.questionnaireSubjectId = questionnaireSubjectId;
        this.name = name;
        this.picId = picId;
        this.isDefault = isDefault;
        this.isRequired = isRequired;
        this.isOther = isOther;
        this.sortNum = sortNum;
        this.questionnaireSubject = questionnaireSubject;
    }

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @TableGenerator(name = "jc_sys_vote_subject_option", pkColumnValue = "jc_sys_vote_subject_option", initialValue = 0, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_vote_subject_option")
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "vote_subject_id", nullable = false, length = 11)
    public Integer getQuestionnaireSubjectId() {
        return questionnaireSubjectId;
    }

    public void setQuestionnaireSubjectId(Integer questionnaireSubjectId) {
        this.questionnaireSubjectId = questionnaireSubjectId;
    }

    @Column(name = "name", nullable = false, length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "pic", nullable = true, length = 11)
    public Integer getPicId() {
        return picId;
    }

    public void setPicId(Integer picId) {
            this.picId = picId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pic", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ResourcesSpaceData getPic() {
        return pic;
    }

    public void setPic(ResourcesSpaceData pic) {
        this.pic = pic;
    }

    @Column(name = "is_default", nullable = false, length = 1)
    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Column(name = "is_required", nullable = false, length = 1)
    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    @Column(name = "is_other", nullable = false, length = 1)
    public Boolean getIsOther() {
        return isOther;
    }

    public void setIsOther(Boolean isOther) {
        this.isOther = isOther;
    }

    @Column(name = "sort_num", nullable = true, length = 6)
    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "vote_subject_id", insertable = false, updatable = false)
    public SysQuestionnaireSubject getQuestionnaireSubject() {
        return questionnaireSubject;
    }

    public void setQuestionnaireSubject(SysQuestionnaireSubject questionnaireSubject) {
        this.questionnaireSubject = questionnaireSubject;
    }

    @Transient
    public String getPicUrl() {
        if (getPic() != null) {
            return getPic().getUrl();
        }
        return "";
    }
}
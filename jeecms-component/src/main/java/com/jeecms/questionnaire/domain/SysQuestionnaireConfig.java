/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Entity
@Table(name = "jc_sys_vote_config")
public class SysQuestionnaireConfig extends AbstractDomain<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 封面图片
     */
    private Integer coverPicId;
    private ResourcesSpaceData coverPic;
    /**
     * 是否使用验证码
     */
    private Boolean isVerification;
    /**
     * 提交成功后处理方式（1显示文字信息2跳转到指定页面3显示结果）
     */
    private Short processType;
    /**
     * 文字信息/指定页面
     */
    private String prompt;
    /**
     * 工作流id
     */
    private Integer workflowId;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 是否登录后才能投票
     */
    private Boolean answerLimit;
    /**
     * 用户答题次数限制
     */
    private Integer userAnswerFrequencyLimit;
    /**
     * 用户答题次数限制单位（1只能2每小时3每天）
     */
    private Short userAnswerFrequencyLimitUnit;
    /**
     * ip答题次数限制
     */
    private Integer ipAnswerFrequencyLimit;
    /**
     * ip答题次数限制单位（1只能2每小时3每天）
     */
    private Short ipAnswerFrequencyLimitUnit;
    /**
     * 设备答题次数限制
     */
    private Integer deviceAnswerFrequencyLimit;
    /**
     * 设备答题次数限制单位（1只能2每小时3每天）
     */
    private Short deviceAnswerFrequencyLimitUnit;
    /**
     * 是否只能微信使用
     */
    private Boolean isOnlyWechat;
    /**
     * 微信用户答题次数限制
     */
    private Integer wechatAnswerFrequencyLimit;
    /**
     * 微信用户答题次数限制（1只能2每小时3每天）
     */
    private Short wechatAnswerFrequencyLimitUnit;
    /**
     * 分享logo图
     */
    private Integer shareLogoId;

    private ResourcesSpaceData shareLogo;
    /**
     * 说明文字
     */
    private String description;

    private SysQuestionnaire questionnaire;

    public SysQuestionnaireConfig() {
    }

    public SysQuestionnaireConfig(Boolean isVerification, Short processType,
                                  String prompt, Integer workflowId, Boolean answerLimit,
                                  Integer userAnswerFrequencyLimit, Short userAnswerFrequencyLimitUnit,
                                  Integer ipAnswerFrequencyLimit, Short ipAnswerFrequencyLimitUnit,
                                  Integer deviceAnswerFrequencyLimit, Short deviceAnswerFrequencyLimitUnit,
                                  Boolean isOnlyWechat, Integer wechatAnswerFrequencyLimit,
                                  Short wechatAnswerFrequencyLimitUnit, String description) {
        this.isVerification = isVerification;
        this.processType = processType;
        this.prompt = prompt;
        this.workflowId = workflowId;
        this.answerLimit = answerLimit;
        this.userAnswerFrequencyLimit = userAnswerFrequencyLimit;
        this.userAnswerFrequencyLimitUnit = userAnswerFrequencyLimitUnit;
        this.ipAnswerFrequencyLimit = ipAnswerFrequencyLimit;
        this.ipAnswerFrequencyLimitUnit = ipAnswerFrequencyLimitUnit;
        this.deviceAnswerFrequencyLimit = deviceAnswerFrequencyLimit;
        this.deviceAnswerFrequencyLimitUnit = deviceAnswerFrequencyLimitUnit;
        this.isOnlyWechat = isOnlyWechat;
        this.wechatAnswerFrequencyLimit = wechatAnswerFrequencyLimit;
        this.wechatAnswerFrequencyLimitUnit = wechatAnswerFrequencyLimitUnit;
        this.description = description;
    }

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @TableGenerator(name = "jc_sys_vote_config", pkColumnValue = "jc_sys_vote_config", initialValue = 0, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_vote_config")
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "cover_pic", nullable = true, length = 11)
    public Integer getCoverPicId() {
        return coverPicId;
    }

    public void setCoverPicId(Integer coverPicId) {
        this.coverPicId = coverPicId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pic", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ResourcesSpaceData getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(ResourcesSpaceData coverPic) {
        this.coverPic = coverPic;
    }

    @Column(name = "is_verification", nullable = false, length = 1)
    public Boolean getIsVerification() {
        return isVerification;
    }

    public void setIsVerification(Boolean isVerification) {
        this.isVerification = isVerification;
    }

    @Column(name = "process_type", nullable = false, length = 1)
    public Short getProcessType() {
        return processType;
    }

    public void setProcessType(Short processType) {
        this.processType = processType;
    }

    @Column(name = "prompt", nullable = true, length = 255)
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Column(name = "workflow_id", nullable = true, length = 11)
    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "begin_time", nullable = false, length = 19)
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time", nullable = true, length = 19)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "answer_limit", nullable = false, length = 1)
    public Boolean getAnswerLimit() {
        return answerLimit;
    }

    public void setAnswerLimit(Boolean answerLimit) {
        this.answerLimit = answerLimit;
    }

    @Column(name = "user_answer_limit", nullable = true, length = 6)
    public Integer getUserAnswerFrequencyLimit() {
        return userAnswerFrequencyLimit;
    }

    public void setUserAnswerFrequencyLimit(Integer userAnswerFrequencyLimit) {
        this.userAnswerFrequencyLimit = userAnswerFrequencyLimit;
    }

    @Column(name = "user_answer_limit_unit", nullable = true, length = 1)
    public Short getUserAnswerFrequencyLimitUnit() {
        return userAnswerFrequencyLimitUnit;
    }

    public void setUserAnswerFrequencyLimitUnit(Short userAnswerFrequencyLimitUnit) {
        this.userAnswerFrequencyLimitUnit = userAnswerFrequencyLimitUnit;
    }

    @Column(name = "ip_answer_limit", nullable = false, length = 6)
    @NotNull
    public Integer getIpAnswerFrequencyLimit() {
        return ipAnswerFrequencyLimit;
    }

    public void setIpAnswerFrequencyLimit(Integer ipAnswerFrequencyLimit) {
        this.ipAnswerFrequencyLimit = ipAnswerFrequencyLimit;
    }

    @Column(name = "ip_answer_limit_unit", nullable = false, length = 1)
    public Short getIpAnswerFrequencyLimitUnit() {
        return ipAnswerFrequencyLimitUnit;
    }

    public void setIpAnswerFrequencyLimitUnit(Short ipAnswerFrequencyLimitUnit) {
        this.ipAnswerFrequencyLimitUnit = ipAnswerFrequencyLimitUnit;
    }

    @Column(name = "device_answer_limit", nullable = false, length = 6)
    public Integer getDeviceAnswerFrequencyLimit() {
        return deviceAnswerFrequencyLimit;
    }

    public void setDeviceAnswerFrequencyLimit(Integer deviceAnswerFrequencyLimit) {
        this.deviceAnswerFrequencyLimit = deviceAnswerFrequencyLimit;
    }

    @Column(name = "device_answer_limit_unit", nullable = false, length = 1)
    @NotNull
    public Short getDeviceAnswerFrequencyLimitUnit() {
        return deviceAnswerFrequencyLimitUnit;
    }

    public void setDeviceAnswerFrequencyLimitUnit(Short deviceAnswerFrequencyLimitUnit) {
        this.deviceAnswerFrequencyLimitUnit = deviceAnswerFrequencyLimitUnit;
    }

    @Column(name = "is_only_wechat", nullable = false, length = 1)
    public Boolean getIsOnlyWechat() {
        return isOnlyWechat;
    }

    public void setIsOnlyWechat(Boolean isOnlyWechat) {
        this.isOnlyWechat = isOnlyWechat;
    }

    @Column(name = "wechat_answer_limit", nullable = true, length = 6)
    public Integer getWechatAnswerFrequencyLimit() {
        return wechatAnswerFrequencyLimit;
    }

    public void setWechatAnswerFrequencyLimit(Integer wechatAnswerFrequencyLimit) {
        this.wechatAnswerFrequencyLimit = wechatAnswerFrequencyLimit;
    }

    @Column(name = "wechat_answer_limit_unit", nullable = true, length = 1)
    public Short getWechatAnswerFrequencyLimitUnit() {
        return wechatAnswerFrequencyLimitUnit;
    }

    public void setWechatAnswerFrequencyLimitUnit(Short wechatAnswerFrequencyLimitUnit) {
        this.wechatAnswerFrequencyLimitUnit = wechatAnswerFrequencyLimitUnit;
    }

    @Column(name = "share_logo", nullable = true, length = 11)
    public Integer getShareLogoId() {
        return shareLogoId;
    }

    public void setShareLogoId(Integer shareLogoId) {
        this.shareLogoId = shareLogoId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_logo", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ResourcesSpaceData getShareLogo() {
        return shareLogo;
    }

    public void setShareLogo(ResourcesSpaceData shareLogo) {
        this.shareLogo = shareLogo;
    }

    @Column(name = "description", nullable = true, length = 150)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    public SysQuestionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(SysQuestionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SysQuestionnaireConfig config = (SysQuestionnaireConfig) o;

        if (id != null ? !id.equals(config.id) : config.id != null) {
            return false;
        }
        if (coverPicId != null ? !coverPicId.equals(config.coverPicId) : config.coverPicId != null) {
            return false;
        }
        if (isVerification != null ? !isVerification.equals(config.isVerification) : config.isVerification != null) {
            return false;
        }
        if (processType != null ? !processType.equals(config.processType) : config.processType != null) {
            return false;
        }
        if (prompt != null ? !prompt.equals(config.prompt) : config.prompt != null) {
            return false;
        }
        if (workflowId != null ? !workflowId.equals(config.workflowId) : config.workflowId != null) {
            return false;
        }
        if (beginTime != null ? !beginTime.equals(config.beginTime) : config.beginTime != null) {
            return false;
        }
        if (endTime != null ? !endTime.equals(config.endTime) : config.endTime != null) {
            return false;
        }
        if (answerLimit != null ? !answerLimit.equals(config.answerLimit) : config.answerLimit != null) {
            return false;
        }
        if (userAnswerFrequencyLimit != null ? !userAnswerFrequencyLimit.equals(config.userAnswerFrequencyLimit) : config.userAnswerFrequencyLimit != null) {
            return false;
        }
        if (userAnswerFrequencyLimitUnit != null ? !userAnswerFrequencyLimitUnit.equals(config.userAnswerFrequencyLimitUnit) : config.userAnswerFrequencyLimitUnit != null) {
            return false;
        }
        if (ipAnswerFrequencyLimit != null ? !ipAnswerFrequencyLimit.equals(config.ipAnswerFrequencyLimit) : config.ipAnswerFrequencyLimit != null) {
            return false;
        }
        if (ipAnswerFrequencyLimitUnit != null ? !ipAnswerFrequencyLimitUnit.equals(config.ipAnswerFrequencyLimitUnit) : config.ipAnswerFrequencyLimitUnit != null) {
            return false;
        }
        if (deviceAnswerFrequencyLimit != null ? !deviceAnswerFrequencyLimit.equals(config.deviceAnswerFrequencyLimit) : config.deviceAnswerFrequencyLimit != null) {
            return false;
        }
        if (deviceAnswerFrequencyLimitUnit != null ? !deviceAnswerFrequencyLimitUnit.equals(config.deviceAnswerFrequencyLimitUnit) : config.deviceAnswerFrequencyLimitUnit != null) {
            return false;
        }
        if (isOnlyWechat != null ? !isOnlyWechat.equals(config.isOnlyWechat) : config.isOnlyWechat != null) {
            return false;
        }
        if (wechatAnswerFrequencyLimit != null ? !wechatAnswerFrequencyLimit.equals(config.wechatAnswerFrequencyLimit) : config.wechatAnswerFrequencyLimit != null) {
            return false;
        }
        if (wechatAnswerFrequencyLimitUnit != null ? !wechatAnswerFrequencyLimitUnit.equals(config.wechatAnswerFrequencyLimitUnit) : config.wechatAnswerFrequencyLimitUnit != null) {
            return false;
        }
        if (shareLogoId != null ? !shareLogoId.equals(config.shareLogoId) : config.shareLogoId != null) {
            return false;
        }
        return description != null ? description.equals(config.description) : config.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (coverPicId != null ? coverPicId.hashCode() : 0);
        result = 31 * result + (isVerification != null ? isVerification.hashCode() : 0);
        result = 31 * result + (processType != null ? processType.hashCode() : 0);
        result = 31 * result + (prompt != null ? prompt.hashCode() : 0);
        result = 31 * result + (workflowId != null ? workflowId.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (answerLimit != null ? answerLimit.hashCode() : 0);
        result = 31 * result + (userAnswerFrequencyLimit != null ? userAnswerFrequencyLimit.hashCode() : 0);
        result = 31 * result + (userAnswerFrequencyLimitUnit != null ? userAnswerFrequencyLimitUnit.hashCode() : 0);
        result = 31 * result + (ipAnswerFrequencyLimit != null ? ipAnswerFrequencyLimit.hashCode() : 0);
        result = 31 * result + (ipAnswerFrequencyLimitUnit != null ? ipAnswerFrequencyLimitUnit.hashCode() : 0);
        result = 31 * result + (deviceAnswerFrequencyLimit != null ? deviceAnswerFrequencyLimit.hashCode() : 0);
        result = 31 * result + (deviceAnswerFrequencyLimitUnit != null ? deviceAnswerFrequencyLimitUnit.hashCode() : 0);
        result = 31 * result + (isOnlyWechat != null ? isOnlyWechat.hashCode() : 0);
        result = 31 * result + (wechatAnswerFrequencyLimit != null ? wechatAnswerFrequencyLimit.hashCode() : 0);
        result = 31 * result + (wechatAnswerFrequencyLimitUnit != null ? wechatAnswerFrequencyLimitUnit.hashCode() : 0);
        result = 31 * result + (shareLogoId != null ? shareLogoId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    /**
     * 封面图地址
     *
     * @return String
     */
    @Transient
    public String getCoverPicUrl() {
        if (getCoverPic() != null) {
            return getCoverPic().getUrl();
        }
        return "";
    }

    /**
     * 分享logo地址
     *
     * @return String
     */
    @Transient
    public String getShareLogoUrl() {
        if (getShareLogo() != null) {
            return getShareLogo().getUrl();
        }
        return "";
    }
}
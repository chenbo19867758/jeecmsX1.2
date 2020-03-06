/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/22 11:29
 */
public class SysQuestionnaireConfigDto {
    /**
     * 封面图片
     */
    private String coverPic;
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
    private String shareLogo;
    /**
     * 说明文字
     */
    private String description;

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Boolean getVerification() {
        return isVerification;
    }

    public void setVerification(Boolean verification) {
        isVerification = verification;
    }

    public Short getProcessType() {
        return processType;
    }

    public void setProcessType(Short processType) {
        this.processType = processType;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getAnswerLimit() {
        return answerLimit;
    }

    public void setAnswerLimit(Boolean answerLimit) {
        this.answerLimit = answerLimit;
    }

    public Integer getUserAnswerFrequencyLimit() {
        return userAnswerFrequencyLimit;
    }

    public void setUserAnswerFrequencyLimit(Integer userAnswerFrequencyLimit) {
        this.userAnswerFrequencyLimit = userAnswerFrequencyLimit;
    }

    public Short getUserAnswerFrequencyLimitUnit() {
        return userAnswerFrequencyLimitUnit;
    }

    public void setUserAnswerFrequencyLimitUnit(Short userAnswerFrequencyLimitUnit) {
        this.userAnswerFrequencyLimitUnit = userAnswerFrequencyLimitUnit;
    }

    public Integer getIpAnswerFrequencyLimit() {
        return ipAnswerFrequencyLimit;
    }

    public void setIpAnswerFrequencyLimit(Integer ipAnswerFrequencyLimit) {
        this.ipAnswerFrequencyLimit = ipAnswerFrequencyLimit;
    }

    public Short getIpAnswerFrequencyLimitUnit() {
        return ipAnswerFrequencyLimitUnit;
    }

    public void setIpAnswerFrequencyLimitUnit(Short ipAnswerFrequencyLimitUnit) {
        this.ipAnswerFrequencyLimitUnit = ipAnswerFrequencyLimitUnit;
    }

    public Integer getDeviceAnswerFrequencyLimit() {
        return deviceAnswerFrequencyLimit;
    }

    public void setDeviceAnswerFrequencyLimit(Integer deviceAnswerFrequencyLimit) {
        this.deviceAnswerFrequencyLimit = deviceAnswerFrequencyLimit;
    }

    public Short getDeviceAnswerFrequencyLimitUnit() {
        return deviceAnswerFrequencyLimitUnit;
    }

    public void setDeviceAnswerFrequencyLimitUnit(Short deviceAnswerFrequencyLimitUnit) {
        this.deviceAnswerFrequencyLimitUnit = deviceAnswerFrequencyLimitUnit;
    }

    public Boolean getOnlyWechat() {
        return isOnlyWechat;
    }

    public void setOnlyWechat(Boolean onlyWechat) {
        isOnlyWechat = onlyWechat;
    }

    public Integer getWechatAnswerFrequencyLimit() {
        return wechatAnswerFrequencyLimit;
    }

    public void setWechatAnswerFrequencyLimit(Integer wechatAnswerFrequencyLimit) {
        this.wechatAnswerFrequencyLimit = wechatAnswerFrequencyLimit;
    }

    public Short getWechatAnswerFrequencyLimitUnit() {
        return wechatAnswerFrequencyLimitUnit;
    }

    public void setWechatAnswerFrequencyLimitUnit(Short wechatAnswerFrequencyLimitUnit) {
        this.wechatAnswerFrequencyLimitUnit = wechatAnswerFrequencyLimitUnit;
    }

    public String getShareLogo() {
        return shareLogo;
    }

    public void setShareLogo(String shareLogo) {
        this.shareLogo = shareLogo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
